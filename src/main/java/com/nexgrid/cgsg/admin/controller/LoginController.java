package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.service.LoginService;
import com.nexgrid.cgsg.admin.utils.CommonUtil;
import com.nexgrid.cgsg.admin.utils.SessionUtil;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import com.nexgrid.cgsg.admin.vo.ResultInfo;
import nexgrid_SHA512.Nexgrid_SHA512;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/login")
public class LoginController {
	
	private Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	/**
	 * @Date : 2015. 9. 7.
	 * @작성자 : 
	 * @프로그램 설명 : ID, PW 확인
	 * @개정이력 :
	 */
	@RequestMapping(value = "/loginProcessForAuth", method = RequestMethod.POST)
	@ResponseBody
	public int loginProcessForAuth(@RequestBody LoginInfo loginInfo) {

		logger.debug("=== loginProcessForAuth loginInfo: " + loginInfo.toString());
				
		// pw 암호화
		loginInfo.setMbrPw(Nexgrid_SHA512.encrypt(CommonUtil.nullCheck(loginInfo.getMbrPw())));
		
		int loginInfoListSize = 0;

		try {
			
			List<LoginInfo> loginInfoList = loginService.getLoginInfo(loginInfo);
			loginInfoListSize = loginInfoList.size();

			// 데이터가 1개 있을 경우에만 로그인 처리
			if(loginInfoListSize == 1) {
				
				LoginInfo loginInfoResult = loginInfoList.get(0);
				int pwdFailCnt = CommonUtil.getNullValue(loginInfoResult.getLoginFailCnt());
				
				Date curDate = new Date(); 
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm"); //요청시간을 Date로 parsing 후 time가져오기 
				
				Date reqDate = dateFormat.parse(loginInfoResult.getLoginFailDt()); 
				long reqDateTime = reqDate.getTime(); //현재시간을 요청시간의 형태로 format 후 time 가져오기 
				curDate = dateFormat.parse(dateFormat.format(curDate)); long curDateTime = curDate.getTime(); //분으로 표현 
				long minute = (curDateTime - reqDateTime) / 60000; 
				
				// 사용여부 Y
				if("Y".equals(loginInfoResult.getUseYn())) {
					
					// failCnt 5회이상
					if(pwdFailCnt >= 5) {
						
						// 10분 초과
						if(minute > 10) {
							loginService.setUserUnLock(loginInfo);
							logger.info("###### 로그인전 인증 성공   ######:" + loginInfo.toString());
						} else {
							loginInfoListSize = -2;
							logger.info("###### 로그인전 인증 실패 (10분 미만 잠금상태) ######");
						}
						
					// failCnt 5회 미만
					} else {
						loginService.setUserUnLock(loginInfo);
						logger.info("###### 로그인전 인증 성공   ######:" + loginInfo.toString());
					}
					
				// 사용여부 N
				} else {
					
					loginInfoListSize = -2;
					
					MbrInfo mbrInfo = new MbrInfo();
					mbrInfo.setMbrId(loginInfo.getMbrId());
					
					// 10분 초과 5회 이상 계정 잠금해제 
					if(minute > 10 && pwdFailCnt >= 5) {
						logger.info("###### 로그인전 인증 성공  ######");
						loginService.setUserUnLock(loginInfo);
						loginInfoListSize = 1;
					} else if(pwdFailCnt == 0){
						logger.info("###### 로그인 실패 - 아이디 잠금상태 ");					
						loginInfoListSize = -3;
					} else {
						logger.info("###### 로그인전 인증 실패 (10분 미만 잠금상태) ######");
					}
				}
				
			} else {
				logger.info("###### 로그인 실패  ######");

				int loginInfoOnlyIdListSize = 0;

				// 아이디로만 사용자 정보 조회
				List<LoginInfo> loginInfoOnlyIdList = loginService.getLoginInfoOnlyId(loginInfo);
				loginInfoOnlyIdListSize = loginInfoOnlyIdList.size();

				if(loginInfoOnlyIdListSize == 1) {// 건수 있으면
					// 아이디로 오류회수 조회
					LoginInfo loginInfoResult = loginInfoOnlyIdList.get(0);
					int pwdFailCnt = CommonUtil.getNullValue(loginInfoResult.getLoginFailCnt());
					loginInfo.setLoginFailCnt(pwdFailCnt + "");
					String useYn = loginInfoResult.getUseYn();

					logger.info("### 비밀번호 오류회수 , 잠금상태 :" + pwdFailCnt + ", " + useYn);

					if("Y".equals(useYn)) {
						
						if(pwdFailCnt >= 5) {	// 5회 오류나면 아이디 잠금 ( 사용여부 N 처리)
							
							int lockCnt = loginService.setUserLock(loginInfo);
							
							if(lockCnt > 0) {
								logger.info("### 로그인 실패 - 아이디 잠금 성공");
								loginInfoListSize = -2;
							} else {
								logger.info("### 로그인 실패 - 아이디 잠금 실패 ");
							}
							
						} else {
							// SYSDATE set
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							String strDate = dateFormat.format(Calendar.getInstance().getTime());
							logger.debug("=== sysdate : " + strDate);
							
							loginInfo.setLoginFailDt(strDate);
							
							// 비밀번호 오류 회수 +1
							int uptCnt = loginService.updateLoginFailCnt(loginInfo);
							logger.debug("### 비밀번호 오류 회수 없데이트 : " + uptCnt);
						}
						
					} else {
						
						loginInfoListSize = -2;
						
						Date curDate = new Date();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm"); //요청시간을 Date로 parsing 후 time가져오기 
						
						Date reqDate = dateFormat.parse(loginInfoResult.getLoginFailDt()); 
						long reqDateTime = reqDate.getTime(); //현재시간을 요청시간의 형태로 format 후 time 가져오기 
						
						curDate = dateFormat.parse(dateFormat.format(curDate)); 
						
						long curDateTime = curDate.getTime(); //분으로 표현 
						long minute = (curDateTime - reqDateTime) / 60000; 

						MbrInfo mbrInfo = new MbrInfo();
						
						mbrInfo.setMbrId(loginInfo.getMbrId());
						// 비밀번호 오류 횟수 5회이상 체크 추가(2019.05.03)
						// 10분 뒤 계정 잠금해제 
						if(minute > 10 && pwdFailCnt >= 5) {
							logger.info("### 로그인실패 - 10분경과 아이디 잠금해제 ");
							loginService.setUserUnLock(loginInfo);
							loginInfoListSize = -1;
							
							// SYSDATE set
							SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							String strDate = dateFormat2.format(Calendar.getInstance().getTime());
							logger.debug("=== sysdate : " + strDate);
							
							loginInfo.setLoginFailDt(strDate);
							
							// 비밀번호 오류 회수 +1
							loginInfo.setLoginFailCnt("0");
							int uptCnt = loginService.updateLoginFailCnt(loginInfo);
							logger.debug("### 비밀번호 오류 회수 없데이트 : " + uptCnt);

							
						} else {
							logger.info("#### 로그인 실패 - 아이디 잠금상태 ");
							loginInfoListSize = -2;
						}
						
					}
				} else {
					logger.info("### 사용자 아이디 없음");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return loginInfoListSize;
	}
	
	/**
	 * @Date : 2017. 1. 18.
	 * @작성자 : 이충건
	 * @프로그램 설명 : 비밀번호 3개월 이전 수정 체크
	 * @개정이력 :
	 */
	@RequestMapping(value = "/getApplyDate", method = RequestMethod.POST)
	@ResponseBody
	public boolean getApplyDate(@ModelAttribute MbrInfo mbrInfo, HttpServletRequest request, Model model) {

		String result = "";

		logger.debug("=== getApplyDate mbrId:" + mbrInfo.getMbrId());

		result = CommonUtil.nullCheck(loginService.getApplyDate(mbrInfo));
		boolean before3M = true;
		
		if(!"".equals(result)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long curTime = System.currentTimeMillis();
			String todayStr = sdf.format(new Date(curTime));
			try {
				Date applyDate = sdf.parse(result);
				Date todayDate = sdf.parse(todayStr);
				logger.debug("=== 현재시간 :" + sdf.format(todayDate));
				todayDate.setMonth(todayDate.getMonth() - 3);

				String a = sdf.format(applyDate);
				String b = sdf.format(todayDate);

				logger.debug("=== 입력된 시간 :" + a);
				logger.debug("=== 현재시간-3M :" + b);

				if(applyDate.after(todayDate)) {
					logger.info("=== 3개월 이전");
					before3M = true;
				} else {
					logger.info("=== 3개월 이후");
					before3M = false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if(before3M) {
				return true;
			} else {
				return false;
			}

		} else {
			logger.debug("=== 입력된 시간 없음:" + result);
			return false;
		}
	}

	@RequestMapping(value = "/goLoginFinal", method = RequestMethod.POST)
	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int goLoginFinal(HttpServletRequest request, @ModelAttribute LoginInfo loginInfo) {

		logger.debug("=== goLoginFinal loginInfo: " + loginInfo.toString());

		int loginInfoListSize = 0;

		try {

			List<LoginInfo> loginInfoList = loginService.getLoginInfo2(loginInfo);
			loginInfoListSize = loginInfoList.size();

			// 데이터가 1개 있을 경우에만 로그인 처리
			if(loginInfoListSize == 1) {
				logger.info("###### 로그인 성공   ######");
				logger.debug("=== loginInfo : " + loginInfo.toString());

				// 중복로그인 체크 
				SessionUtil.preventDuplication(loginInfo.getMbrId());
				
				LoginInfo loginInfoResult = loginInfoList.get(0);
				SessionUtil.setSessionInfo(loginInfoResult);
				
				// 접속날짜 SYSDATE set
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String strDate = dateFormat.format(Calendar.getInstance().getTime());
				loginInfo.setLoginEndDt(strDate);
				
				//---------------------------------------------------------------------------
				//전체 하위 메뉴 목록 - 보안 점검사항 추가 : 2017.02.29 kyh
				//SessionInterceptor 에서 처리 로직.
				List<MenuInfo> subMenuListAll = loginService.getAllSubMenuList(loginInfo);
				SessionUtil.setSubMenuListAll(subMenuListAll);
				//---------------------------------------------------------------------------

				// 공통메뉴 처리 - 세션에 'commonMenuList'명으로 공통메뉴 목록 객체를 저장
				List<MenuInfo> commonMenuList = loginService.getCommonMenuList(loginInfo);
				logger.debug("----- 공통메뉴 목록 -----" + commonMenuList.toString());
				SessionUtil.setCommonMenuList(commonMenuList);

				// 각 사용자 권한에 따른 하위메뉴 목록 - 세션에 'subMenuList'명으로 하위메뉴 목록 객체를 저장
				List<MenuInfo> subMenuList = loginService.getSubMenuList2(loginInfo);

				logger.debug("----- 사용자 접근 권한에 따른 하위메뉴 목록 -----" + subMenuList.toString());
				SessionUtil.setSubMenuList(subMenuList);

				// 비밀번호 오류회수 0 초기화
				// --------------------------------------------------------------
				String strLoginFailCnt = CommonUtil.nullCheck(loginInfoResult.getLoginFailCnt());
				int intLoginFailCnt = 0;
				if(!"".equals(strLoginFailCnt))
					intLoginFailCnt = Integer.parseInt(strLoginFailCnt);
				else
					intLoginFailCnt = 0;
				String mbrId = CommonUtil.nullCheck(loginInfoResult.getMbrId());

				logger.debug("### 사용자아이디, 오류회수 : " + mbrId + ", " + intLoginFailCnt);

				if(!"".equals(mbrId) && intLoginFailCnt > 0) {
					
					int i = loginService.setLoginFailCntInit(loginInfo);
					
					if(i > 0) {
						logger.info("###" + mbrId + ": 로그인 성공 ~비밀번호 오류회수 0 초기화 성공");
					} else {
						logger.info("###" + mbrId + ": 로그인 성공 ~비밀번호 오류회수 0 초기화 실패");
					}
					
				} else {
					logger.info("###" + mbrId + ": 로그인 성공 ~비밀번호 오류회수 0 초기화 안함");
				}
				
				// 마지막 접속날짜 등록
				int checkUpdate = loginService.updateLoginEndDt(loginInfo);
				if(checkUpdate == 1) {
					logger.info("###### 마지막 접속일 업데이트  성공  ######:" + strDate);
				}

				return loginInfoListSize;
			
			// 데이터가 1개가 아닐경우
			} else {
				
				logger.info("###### 로그인 실패  ######");

				int loginInfoOnlyIdListSize = 0;

				// 아이디로만 사용자 정보 조회
				List<LoginInfo> loginInfoOnlyIdList = loginService.getLoginInfoOnlyId(loginInfo);
				loginInfoOnlyIdListSize = loginInfoOnlyIdList.size();

				// 건수 있으면
				if(loginInfoOnlyIdListSize == 1) {
					// 아이디로 오류회수 조회
					LoginInfo loginInfoResult = loginInfoOnlyIdList.get(0);
					int pwdFailCnt = CommonUtil.getNullValue(loginInfoResult.getLoginFailCnt());
					loginInfo.setLoginFailCnt(pwdFailCnt + "");
					
					String useYn = loginInfoResult.getUseYn();

					logger.debug("### 비밀번호 오류회수 , 잠금상태 :" + pwdFailCnt + ", " + useYn);
					
					// 사용여부 Y
					if("Y".equals(useYn)) {
						
						if(pwdFailCnt >= 5) {// 5회 오류나면 아이디 잠금 ( 사용여부 N 처리)
							
							int lockCnt = loginService.setUserLock(loginInfo);
							
							if(lockCnt > 0) {
								logger.info("### 로그인 실패 - 아이디 잠금 성공");
								loginInfoListSize = -1;
							} else {
								logger.info("### 로그인 실패 - 아이디 잠금 실패 ");
							}
							
						} else {
							
							// SYSDATE set
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							String strDate = dateFormat.format(Calendar.getInstance().getTime());
							logger.info("=== sysdate : " + strDate);
							
							loginInfo.setLoginFailDt(strDate);
							
							// 비밀번호 오류 회수 +1
							int uptCnt = loginService.updateLoginFailCnt(loginInfo);
							logger.info("### 비밀번호 오류 회수 없데이트 : " + uptCnt);
						}
						
					// 사용여부 N
					} else {
						
						loginInfoListSize = -2;
						
						Date curDate = new Date(); 
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm"); //요청시간을 Date로 parsing 후 time가져오기 
						
						Date reqDate = dateFormat.parse(loginInfoResult.getLoginFailDt()); 
						long reqDateTime = reqDate.getTime(); //현재시간을 요청시간의 형태로 format 후 time 가져오기 
						
						curDate = dateFormat.parse(dateFormat.format(curDate)); 
						
						long curDateTime = curDate.getTime(); //분으로 표현 
						long minute = (curDateTime - reqDateTime) / 60000; 

						MbrInfo mbrInfo = new MbrInfo();
						mbrInfo.setMbrId(loginInfo.getMbrId());
						
						// 10분 뒤 계정 잠금해제 
						if(minute > 10 && pwdFailCnt >= 5) {
							logger.info("### 로그인실패 - 10분경과 아이디 잠금해제 ");
							loginService.setUserUnLock(loginInfo);
							loginInfoListSize = -1;
							
							// SYSDATE set
							SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							String strDate = dateFormat2.format(Calendar.getInstance().getTime());
							logger.info("=== sysdate : " + strDate);
							
							loginInfo.setLoginFailDt(strDate);
							
							// 비밀번호 오류 회수 +1
							loginInfo.setLoginFailCnt("0");
							int uptCnt = loginService.updateLoginFailCnt(loginInfo);
							logger.info("### 비밀번호 오류 회수 업데이트 : " + uptCnt);
							
						} else {
							logger.info("#### 로그인 실패 - 아이디 잠금상태 ");
							loginInfoListSize = -1;
						}
					}
				} else {
					logger.info("### 사용자 아이디 없음");

				}

				return loginInfoListSize;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return loginInfoListSize;
	}

	/**
	 * @Date : 2018. 11. 10.
	 * @작성자 : 
	 * @프로그램 설명 : 세션확인
	 * @개정이력 :
	 */
	@RequestMapping(value = "/session", method = RequestMethod.POST)
	@ResponseBody
	public String authVuew(HttpServletRequest request, Model model) {
	
		logger.info("-------- SessionInterceptor preHandle ----------");
		String reqURI = request.getParameter("uri");
    	logger.debug("-------- SessionInterceptor getRequestURI() ===> " + reqURI);
    	
    	//--------------------------------------------------------------------------------
    	//--------------------------------------------------------------------------------
    	//보안 사항 : 사용자별 URL 변조로 인한 메뉴 획득 방지  - 2017.02.28 kyh
    		
    	//reqURI 이 메뉴마스터  MENU_URL에 있는지 여부 체크 (서브메뉴인지 여부 체크)
    	//전체 하위 메뉴 
    	List<MenuInfo> subMenuListAll = (List<MenuInfo>) SessionUtil.getSubMenuListAll();

    	boolean isSubMenu = false;

    	logger.debug(request.getParameter("uri"));
    	
    	if(subMenuListAll != null ) {
    		for(int i=0 ;i<subMenuListAll.size(); i++) {
    			
    			MenuInfo tempInfo = new MenuInfo();
    			tempInfo = subMenuListAll.get(i);
    			String menuUrl = tempInfo.getMenuURL();
    			
    			if(reqURI.equals(menuUrl)) {
    				isSubMenu = true;
    			}
    		}
    	}
    	// reqURI 이 서브 메뉴이면 권한별 서브메뉴와 비교하여 비정상 요청URL 이면 세션종료 한다. 
    	if(isSubMenu) {
    		logger.info("### 좌측 서브 메뉴 클릭 요청 URL : " + reqURI);
    		//권한별 체크 
    		List<MenuInfo> subMenuList = (List<MenuInfo>) SessionUtil.getSubMenuList();
    		
    		boolean authReq = false;
    		
    		if(subMenuList != null ) {
    			for(int i=0 ;i<subMenuList.size(); i++) {
    				
    				MenuInfo tempInfo = new MenuInfo();
    				tempInfo = subMenuList.get(i);
    				String menuUrl = tempInfo.getMenuURL();
    				
    				if(reqURI.equals(menuUrl)) {
    					authReq = true;
    				}
    			}
    		}
    		
    		logger.info("### 정상 요청URL 여부 : " + authReq);
    		// 요청한 URL이 사용자 권한 URL 이 아니면 세션없애고 로그인 창으로..
    		if(!authReq) {
    			logger.info("### 권한이외의 비정상 요청URL 이므로 세션 종료! : " + reqURI);
    			SessionUtil.sessionDel();
    			return "false";
    		}
    	}
    	
    	if(SessionUtil.isLogin()) {
    		SessionUtil.setSessionTimeOut();
    		return "true";
    	} else {
    		return "false";
    	}
	}
	
	/**
	 * @Date : 2018. 11. 10.
	 * @작성자 : 
	 * @프로그램 설명 : 세션정보조회
	 * @개정이력 :
	 */
	@RequestMapping(value = "/getSessionInfo", method = RequestMethod.POST)
	@ResponseBody
	public LoginInfo sessionInfo(HttpServletRequest request, Model model) {

		logger.info("getSessionInfo");
		
		LoginInfo result = SessionUtil.getSessionInfo();
		
		result.setSubMenuList(SessionUtil.getSubMenuList());
		result.setCommonMenuList(SessionUtil.getCommonMenuList());

		return result;
	}
	

	@RequestMapping(value = "/logoutProcess2", method = RequestMethod.GET)
	@ResponseBody
	public void logoutProcess2(HttpServletRequest request, @ModelAttribute LoginInfo loginInfo, Model model, BindingResult result) {

		logger.info("=== logoutProcess");
		
		SessionUtil.sessionDel();

	}
	
	/**
	 * @Date : 2015. 9. 7.
	 * @작성자 : 김영호
	 * @프로그램 설명 : 비밀번호 변경시 기존 비밀번호 확인
	 * @개정이력 :
	 */
	@RequestMapping(value = "/loginChk", method = RequestMethod.POST)
	@ResponseBody
	public boolean loginChk(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LoginInfo loginInfo) {

		logger.debug("=== loginChk loginInfo: " + loginInfo.toString());

		// pw 암호화
		loginInfo.setMbrPw(Nexgrid_SHA512.encrypt(CommonUtil.nullCheck(loginInfo.getMbrPw())));
		
		int loginInfoListSize = 0;
		boolean rst = false;

		try {

			List<LoginInfo> loginInfoList = loginService.getLoginInfo(loginInfo);
			loginInfoListSize = loginInfoList.size();

			// 데이터가 1개 있을 경우에만 로그인 처리
			if(loginInfoListSize == 1) {
				logger.info("### 기존패스워드 확인 - 성공");
				rst = true;
			} else {
				logger.info("### 기존패스워드 확인 - 실패");
				loginInfoListSize = 0;
				rst = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rst;
	}


	/**
	 * @author PSJ
	 * @Date   2019. 3. 25.
	 * @param  
	 * @기능설명 비밀번호 변경 시 직전3회 체크   
	 *
	 */
	@RequestMapping(value = "/oldInfoChk", method = RequestMethod.POST)
	@ResponseBody
	public boolean oldInfoChk(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LoginInfo loginInfo) {

		logger.debug("=== oldInfoChk loginInfo: " + loginInfo.toString());

		// pw 암호화
		loginInfo.setMbrPw(Nexgrid_SHA512.encrypt(CommonUtil.nullCheck(loginInfo.getMbrPw())));
		
		int oldInfoCnt = 0;
		boolean rst = false;

		try {
			
			oldInfoCnt = loginService.getOldPwValidate(loginInfo);

			if(oldInfoCnt > 0) {
				logger.info("### 기존패스워드에 존재 - 변경불가");
				rst = false;
			} else {
				logger.info("### 기존패스워드에 미존재 - 변경가능");
				rst = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rst;
	}

}
