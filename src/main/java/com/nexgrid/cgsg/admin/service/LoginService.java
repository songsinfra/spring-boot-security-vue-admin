package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.mapper.LoginMapper;
import com.nexgrid.cgsg.admin.utils.CommonUtil;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.apache.commons.lang3.StringUtils;
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

	public LoginInfo login(String userId) {
		LoginInfo loginInfoParams = LoginInfo.builder().mbrId(userId).build();

		List<LoginInfo> loginInfoList = mapper.getLoginInfo(loginInfoParams);

		if (loginInfoList.size() == 0) this.failLogin(loginInfoParams);

		LoginInfo loginInfo = loginInfoList.get(0);
		this.successLogin(loginInfo);

		return loginInfo;
	}

	public void successLogin(LoginInfo loginInfo) {
		Assert.notNull(loginInfo, "selected loginInfo is null");

		if (!"Y".equalsIgnoreCase(loginInfo.getUseYn())) {
			this.lockedLoginChecker(loginInfo.getMbrId(), loginInfo.getLoginFailCnt(), loginInfo.getLoginFailDt());
		}else{
			this.unLockedLoginChecker(loginInfo.getMbrId(), loginInfo.getLoginFailCnt(), loginInfo.getLoginFailDt());
		}

		this.setLoginEndDate(loginInfo);
	}

	public void setLoginEndDate(LoginInfo loginInfo) {
		loginInfo.setLoginEndDt(CommonUtil.getToday());
		mapper.updateLoginEndDt(loginInfo);		// 마지막 접속날짜 등록
	}

	public void lockedLoginChecker(String mbrId, String loginFailCnt, String loginFailDate) {
		Assert.hasLength(mbrId, "mbrId is empty");
		Assert.hasLength(loginFailCnt, "loginFailCnt is empty");
		Assert.hasLength(loginFailDate, "loginFailDate is empty");

		int pwdFailCnt = CommonUtil.getNullValue(loginFailCnt);
		long minute = this.getLeftMinuteBetweenNowDateAnd(loginFailDate);

		// 10분 초과 5회 이상 계정 잠금해제
		if(minute > LOGIN_BLOCK_MINUTE && pwdFailCnt >= PASSWORD_FAIL_COUNT) {
			log.info("===== Success Login =====");
			mapper.setUserUnLockAndResetFailCnt(mbrId);
		} else if(pwdFailCnt == 0){
			throw new AdminException(SystemStatusCode.LOGIN_FAIL_LOCK, "로그인 실패 - 아이디 잠금상태");
		} else {
			throw new AdminException(SystemStatusCode.LOGIN_FAIL_LOCK_LEFT_TIME, "로그인전 인증 실패 (10분 미만 잠금상태)");
		}
	}

	public void unLockedLoginChecker(String mbrId, String loginFailCnt, String loginFailDate) {
		Assert.hasLength(mbrId, "mbrId is empty");

		int pwdFailCnt = CommonUtil.getNullValue(loginFailCnt);
		long minute = this.getLeftMinuteBetweenNowDateAnd(loginFailDate);

		//  실패 n회 이상 로그인제한 n분 이하
		if (pwdFailCnt >= PASSWORD_FAIL_COUNT && minute <= LOGIN_BLOCK_MINUTE) {
			throw new AdminException(SystemStatusCode.LOGIN_FAIL_LOCK_LEFT_TIME, "로그인전 인증 실패 (10분 미만 잠금상태)");
		}

		log.info("===== Success Login =====");
		mapper.setUserUnLockAndResetFailCnt(mbrId);
	}

	public long getLeftMinuteBetweenNowDateAnd(String loginFailDt) {
		if(StringUtils.isEmpty(loginFailDt)) return 0;

		LocalDateTime loginDateTime = LocalDateTime.parse(loginFailDt, DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
		LocalDateTime nowDateTime = LocalDateTime.now();

		return ChronoUnit.MINUTES.between(loginDateTime, nowDateTime);
	}

	public void failLogin(LoginInfo loginInfo) {
		Assert.notNull(loginInfo, "loginInfo is null");

		if ("Y".equalsIgnoreCase(loginInfo.getUseYn())) {
			this.unlockedLoginForFail(loginInfo.getMbrId(), loginInfo.getLoginFailCnt());
		} else {
			this.lockedLoginForFail(loginInfo.getMbrId(), loginInfo.getLoginFailCnt(), loginInfo.getLoginFailDt());
		}

		throw new AdminException(SystemStatusCode.FAIL_LOGIN, "### 로그인 실패");
	}

	public void unlockedLoginForFail(String mbrId, String loginFailCnt) {
		int pwdFailCnt = CommonUtil.getNullValue(loginFailCnt);
		if(pwdFailCnt >= 5) {	// 5회 오류나면 아이디 잠금 ( 사용여부 N 처리)
			int lockCnt = mapper.setUserLock(mbrId);
			throw new AdminException(SystemStatusCode.LOGIN_FAIL_LOCKED_USER_ID, "### 로그인 실패 - 아이디 잠금 성공");
		}

		mapper.updateLoginFailCnt(LoginInfo.builder()
				.mbrId(mbrId)
				.loginFailCnt(String.valueOf(pwdFailCnt))
				.loginFailDt(CommonUtil.getToday())
				.build()
		); // 비밀번호 오류 회수 +1
	}

	public void lockedLoginForFail(String mbrId, String loginFailCnt, String loginFailDate) {
		int pwdFailCnt = CommonUtil.getNullValue(loginFailCnt);
		long minute = this.getLeftMinuteBetweenNowDateAnd(loginFailDate);

		if(minute > LOGIN_BLOCK_MINUTE && pwdFailCnt >= PASSWORD_FAIL_COUNT) {
//			logger.info("### 로그인실패 - 10분경과 아이디 잠금해제 ");
			mapper.setUserUnLockAndResetFailCnt(mbrId); // ### 로그인실패 - 10분경과 아이디 잠금해제

			mapper.updateLoginFailCnt(LoginInfo.builder()
					.mbrId(mbrId)
					.loginFailCnt("0")
					.loginFailDt(CommonUtil.getToday())
					.build());
			//log.debug("### 비밀번호 오류 회수 없데이트 : " + uptCnt);
		} else {
			throw new AdminException(SystemStatusCode.LOGIN_FAIL_LOCK, "### 로그인 실패 - 아이디 잠금상태");
		}
	}


	public boolean isExpirePasswordDuration(MbrInfo mbrInf, LocalDate nowDate) {
		String applyDate = mapper.getApplyDate(mbrInf);
		if(StringUtils.isEmpty(applyDate)) return false;

		LocalDate passwordChangeDate = LocalDate.parse(applyDate, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		LocalDate changeDate = nowDate.minusMonths(PASSWORD_CHANGE_MONTHS);

		return passwordChangeDate.isBefore(changeDate);
	}

	public boolean isExistLoginInfo(LoginInfo loginInfo) {
		Assert.notNull(loginInfo, "loginInfo is null");
		Assert.hasLength(loginInfo.getMbrPw(), "mbrPw is null");
		Assert.hasLength(loginInfo.getMbrId(), "mbrId is null");

		loginInfo.setMbrPw(CommonUtil.convertEncryptPassword(loginInfo.getMbrPw()));
		List<LoginInfo> loginInfoList = mapper.getLoginInfo(loginInfo);
		return loginInfoList.size() == 1;
	}

	public List<LoginInfo> getLoginInfo(LoginInfo loginInfo) {
		return mapper.getLoginInfo(loginInfo);
	}

	public List<MenuInfo> getCommonMenuList(LoginInfo loginInfo) {
		return mapper.getCommonMenuList(loginInfo);
	}

	public int setUserLock(LoginInfo loginInfo) {
		return mapper.setUserLock(loginInfo.getMbrId());
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
		return mapper.setUserUnLockAndResetFailCnt(loginInfo.getMbrId());
	}
	
	public boolean isSamePassword(LoginInfo loginInfo) {

		loginInfo.setMbrPw(CommonUtil.convertEncryptPassword(loginInfo.getMbrPw()));
		int result = mapper.getOldPwValidate(loginInfo);
		return result > 0;
	}

	public int updateLoginEndDt(LoginInfo loginInfo) {
		return mapper.updateLoginEndDt(loginInfo);
	}
}
