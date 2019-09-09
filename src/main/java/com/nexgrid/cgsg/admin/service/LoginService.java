package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.mapper.LoginMapper;
import com.nexgrid.cgsg.admin.utils.CommonUtil;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service(value = "loginService")
public class LoginService {
	public static final int PASSWORD_CHANGE_MONTHS = 3;
	public static final int LOGIN_BLOCK_MINUTE = 10;
	public static final int PASSWORD_FAIL_COUNT = 5;
	private Logger log = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private LoginMapper mapper;

	public LoginInfo login(LoginInfo loginInfoParams) {
		List<LoginInfo> loginInfoList = mapper.getLoginInfo(loginInfoParams);

		if (loginInfoList.size() == 0) this.failLogin(loginInfoParams);

		LoginInfo loginInfo = loginInfoList.get(0);
		this.successLogin(loginInfo);

		return loginInfo;
	}

	public void successLogin(LoginInfo loginInfo) {
		Assert.notNull(loginInfo, "selected loginInfo is null");

		if (!"Y".equalsIgnoreCase(loginInfo.getUseYn())) {
			this.lockedLogin(loginInfo);
		}else{
			this.unLockedLogin(loginInfo);
		}

		this.setMenuListTo(loginInfo);
		this.setLoginEndDate(loginInfo);
	}

	public void setLoginEndDate(LoginInfo loginInfo) {
		loginInfo.setLoginEndDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		mapper.updateLoginEndDt(loginInfo);		// 마지막 접속날짜 등록
	}

	public void lockedLogin(LoginInfo loginInfo) {
		int pwdFailCnt = CommonUtil.getNullValue(loginInfo.getLoginFailCnt());
		long minute = this.getLeftMinuteBetweenNowDateAnd(loginInfo.getLoginFailDt());

		// 10분 초과 5회 이상 계정 잠금해제
		if(minute > LOGIN_BLOCK_MINUTE && pwdFailCnt >= PASSWORD_FAIL_COUNT) {
			log.info("===== Success Login =====");
			mapper.setUserUnLockAndResetFailCnt(loginInfo);
		} else if(pwdFailCnt == 0){
			throw new AdminException("2003", "로그인 실패 - 아이디 잠금상태");
		} else {
			throw new AdminException("2002", "로그인전 인증 실패 (10분 미만 잠금상태)");
		}
	}

	public void unLockedLogin(LoginInfo loginInfo) {
		int pwdFailCnt = CommonUtil.getNullValue(loginInfo.getLoginFailCnt());
		long minute = this.getLeftMinuteBetweenNowDateAnd(loginInfo.getLoginFailDt());

		if (pwdFailCnt >= PASSWORD_FAIL_COUNT && minute <= LOGIN_BLOCK_MINUTE) {
			throw new AdminException("2002", "로그인전 인증 실패 (10분 미만 잠금상태)");
		}

		log.info("===== Success Login =====");
		mapper.setUserUnLockAndResetFailCnt(loginInfo);
	}

	public long getLeftMinuteBetweenNowDateAnd(String loginFailDt) {
		LocalDateTime loginDateTime = LocalDateTime.parse(loginFailDt, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
		LocalDateTime nowDateTime = LocalDateTime.now();

		return ChronoUnit.MINUTES.between(loginDateTime, nowDateTime);
	}

	public void failLogin(LoginInfo loginInfoParam) {
		List<LoginInfo> loginInfoList = mapper.getLoginInfoOnlyId(loginInfoParam); // 아이디로만 사용자 정보 조회

		if (loginInfoList.size() == 0) throw new AdminException("2000", "사용자 아이디 없음");

		LoginInfo loginInfo = loginInfoList.get(0);
		if ("Y".equalsIgnoreCase(loginInfo.getUseYn())) {
			this.unlockedLoginForFail(loginInfo);
		} else {
			this.lockedLoginForFail(loginInfo);
		}

		throw new AdminException("2009", "### 로그인 실패");
	}

	public void unlockedLoginForFail(LoginInfo loginInfo) {
		int pwdFailCnt = CommonUtil.getNullValue(loginInfo.getLoginFailCnt());
		if(pwdFailCnt >= 5) {	// 5회 오류나면 아이디 잠금 ( 사용여부 N 처리)
			int lockCnt = mapper.setUserLock(loginInfo);
			throw new AdminException("2009", "### 로그인 실패 - 아이디 잠금 성공");
		}

		LocalDateTime nowDateTime = LocalDateTime.now();
		loginInfo.setLoginFailDt(nowDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

		mapper.updateLoginFailCnt(loginInfo); // 비밀번호 오류 회수 +1
	}

	public void lockedLoginForFail(LoginInfo loginInfo) {
		int pwdFailCnt = CommonUtil.getNullValue(loginInfo.getLoginFailCnt());
		long minute = this.getLeftMinuteBetweenNowDateAnd(loginInfo.getLoginFailDt());

		if(minute > LOGIN_BLOCK_MINUTE && pwdFailCnt >= PASSWORD_FAIL_COUNT) {
//			logger.info("### 로그인실패 - 10분경과 아이디 잠금해제 ");
			mapper.setUserUnLockAndResetFailCnt(loginInfo);
//			loginInfoListSize = -1;

			// SYSDATE set
			LocalDateTime nowDateTime = LocalDateTime.now();
			loginInfo.setLoginFailDt(nowDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

			// 비밀번호 오류 회수 +1
			loginInfo.setLoginFailCnt("0");
			int uptCnt = mapper.updateLoginFailCnt(loginInfo);
			//log.debug("### 비밀번호 오류 회수 없데이트 : " + uptCnt);
		} else {
			throw new AdminException("2009", "### 로그인 실패 - 아이디 잠금상태");
		}
	}


	public boolean isExpirePasswordDuration(MbrInfo mbrInf, LocalDate nowDate) {
		String applyDate = mapper.getApplyDate(mbrInf);
		Assert.hasLength(applyDate, "require selected applyDate");

		LocalDate passwordChangeDate = LocalDate.parse(applyDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
		LocalDate changeDate = nowDate.minusMonths(PASSWORD_CHANGE_MONTHS);

		return changeDate.isBefore(passwordChangeDate);
	}

	public LoginInfo setMenuListTo(LoginInfo loginInfo) {
		//Map menuList = new HashMap();

		//---------------------------------------------------------------------------
		//전체 하위 메뉴 목록 - 보안 점검사항 추가 : 2017.02.29 kyh
		//SessionInterceptor 에서 처리 로직.
//		List<MenuInfo> subMenuListAll = mapper.getAllSubMenuList(loginInfo);
//		menuList.put("subMenuListAll", subMenuListAll);
		//---------------------------------------------------------------------------

		// 공통메뉴 처리 - 세션에 'commonMenuList'명으로 공통메뉴 목록 객체를 저장
		List<MenuInfo> commonMenuList = mapper.getCommonMenuList(loginInfo);
		loginInfo.setCommonMenuList(commonMenuList);

		// 각 사용자 권한에 따른 하위메뉴 목록 - 세션에 'subMenuList'명으로 하위메뉴 목록 객체를 저장
		List<MenuInfo> subMenuList = mapper.getSubMenuList2(loginInfo);
		loginInfo.setSubMenuList(subMenuList);

		return loginInfo;
	}


	public List<LoginInfo> getLoginInfo(LoginInfo loginInfo) {
		return mapper.getLoginInfo(loginInfo);
	}

	public List<MenuInfo> getCommonMenuList(LoginInfo loginInfo) {
		return mapper.getCommonMenuList(loginInfo);
	}

	public int setUserLock(LoginInfo loginInfo) {
		return mapper.setUserLock(loginInfo);
	}

	public int updateLoginFailCnt(LoginInfo loginInfo) {
		return mapper.updateLoginFailCnt(loginInfo);
	}

	public int setLoginFailCntInit(LoginInfo loginInfo) {
		return mapper.setLoginFailCntInit(loginInfo);
	}

	public List<LoginInfo> getLoginInfoOnlyId(LoginInfo loginInfo) {
		return mapper.getLoginInfoOnlyId(loginInfo);
	}

	public String getApplyDate(MbrInfo mbrInfo) {
		return mapper.getApplyDate(mbrInfo);
	}

	public List<LoginInfo> getLoginInfo2(LoginInfo loginInfo) {
		return mapper.getLoginInfo2(loginInfo);
	}

	public List<MenuInfo> getSubMenuList2(LoginInfo loginInfo) {
		return mapper.getSubMenuList2(loginInfo);
	}

	public List<MenuInfo> getAllSubMenuList(LoginInfo loginInfo) {
		return mapper.getAllSubMenuList(loginInfo);
	}

	public int setUserUnLock(LoginInfo loginInfo) {
		return mapper.setUserUnLockAndResetFailCnt(loginInfo);
	}
	
	public int getOldPwValidate(LoginInfo loginInfo) {
		return mapper.getOldPwValidate(loginInfo);
	}

	public int updateLoginEndDt(LoginInfo loginInfo) {
		return mapper.updateLoginEndDt(loginInfo);
	}
}
