package com.nexgrid.cgsg.admin.utils;

import com.nexgrid.cgsg.admin.vo.LoginInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


/**
 * @Date         : 2015. 9. 7. 
 * @작성자      : 김영호
 * @프로그램 설명 : 세션에 대하여 처리
 * @개정이력 :
 */
 
public class SessionUtil implements HttpSessionListener {

	private static Logger logger  = LoggerFactory.getLogger(SessionUtil.class);
	
	// 세션테이블
	private static Hashtable sessionInfo = new Hashtable();
	
	public static HttpSession session() {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(true);
	}
	
	public static void setSessionTimeOut(){
		session().setMaxInactiveInterval(60*30); //30분 
	}
	
	
	// 화면 테스트용 시작-------------------------------------------------------------------------------------------------------------
	
	public static void setSessionInfo(LoginInfo loginInfo){
		// 테스트용
		session().getServletContext().getContext("/admin").setAttribute("loginInfo", loginInfo); 
	}
	
	public static LoginInfo getSessionInfo(){
		// 테스트용
		return (LoginInfo)session().getServletContext().getContext("/admin").getAttribute("loginInfo");
	}
	
	public static boolean isLogin(){
		// 테스트용
		if ( session().getServletContext().getContext("/admin").getAttribute("loginInfo") == null ){
			return false;
		} else {
			return true;
		}
	}
	
	// 세션삭제
	public static void sessionDel() {
		sessionInfo.remove(session());
		session().invalidate();
	}

	public static void setCommonMenuList(List<?> commonMenuList) {
		// 테스트용
		session().getServletContext().getContext("/admin").setAttribute("commonMenuList", commonMenuList);
	}

	public static List<?> getCommonMenuList() {
		// 테스트용
		return (List<?>)session().getServletContext().getContext("/admin").getAttribute("commonMenuList");
	}

	public static void setSubMenuList(List<?> subMenuList) {
		// 테스트용
		session().getServletContext().getContext("/admin").setAttribute("subMenuList", subMenuList);
	}
	
	public static List<?> getSubMenuList() {
		// 테스트용
		return (List<?>)session().getServletContext().getContext("/admin").getAttribute("subMenuList");
	}

	public static void setSubMenuListAll(List<?> subMenuListAll) {
		// 테스트용
		session().getServletContext().getContext("/admin").setAttribute("subMenuListAll", subMenuListAll);
	}

	public static List<?> getSubMenuListAll() {
		// 테스트용
		return (List<?>)session().getServletContext().getContext("/admin").getAttribute("subMenuListAll");
	}
	
	// 화면 테스트용 끝--------------------------------------------------------------------------------------------------------------
	
	
	
	
	// 실제 사용 시작-------------------------------------------------------------------------------------------------------------
	/*
	public static void setSessionInfo(LoginInfo loginInfo){
		session().setAttribute("loginInfo", loginInfo); 
	}
	
	public static LoginInfo getSessionInfo(){
		return (LoginInfo)session().getAttribute("loginInfo");
	}
	
	public static boolean isLogin(){
		if ( session().getAttribute("loginInfo") == null ){
			return false;
		} else {
			return true;
		}
	}
	
	// 세션삭제
	public static void sessionDel() {
		sessionInfo.remove(session());
		session().invalidate();
	}
	
	public static void setCommonMenuList(List<?> commonMenuList) {
		session().setAttribute("commonMenuList", commonMenuList);
	}
	
	public static List<?> getCommonMenuList() {
		return (List<?>)session().getAttribute("commonMenuList");
	}
	
	public static void setSubMenuList(List<?> subMenuList) {
		session().setAttribute("subMenuList", subMenuList);
	}
	
	public static List<?> getSubMenuList() {
		return (List<?>)session().getAttribute("subMenuList");
	}
	
	public static void setSubMenuListAll(List<?> subMenuListAll) {
		session().setAttribute("subMenuListAll", subMenuListAll);
	}
	
	public static List<?> getSubMenuListAll() {
		return (List<?>)session().getAttribute("subMenuListAll");
	}
	*/
	// 실제 사용 끝--------------------------------------------------------------------------------------------------------------

	
	/**
	 * @param  mbrId
	 * @기능설명  중복세션 체크 - 존재하면 기존 세션 삭제
	 *
	 */
	public static void preventDuplication(String mbrId) {
		
		if(!isUsing(mbrId)) {
			// 세션 생성
			HttpSession session = session();
			setSession(session, mbrId);
			
		} else {
			logger.info("### 중복로그인 - 기존접속 세션 제거");
			sessionDel(mbrId);
	
			// 세션 생성
			HttpSession session = session();
			setSession(session, mbrId);			
		}
	}

	/**
	 * @param  mbrId
	 * @기능설명 중복로그인 시 mbrId로 기존세션 제거   
	 *
	 */
	public static void sessionDel(String mbrId) {
		Enumeration e = sessionInfo.keys();
		HttpSession session = null;
		
		while(e.hasMoreElements()) {

			session = (HttpSession) e.nextElement();
			if(sessionInfo.get(session).equals(mbrId)) {				
				sessionInfo.remove(session);
				session.invalidate();
			}
		}
	}
	

	/**
	 * @param  mbrId
	 * @기능설명   이미 사용중인 아이디인지 확인
	 * 			사용중인 경우 true, 사용중이 아니면 false
	 *
	 */
	public static boolean isUsing(String mbrId) {
		return sessionInfo.containsValue(mbrId);
	}
	
	
	/**
	 * @param  session, mbrId
	 * @기능설명 로그인 완료한 사용자의 아이디를 세션테이블에 저장  
	 *
	 */
	public static void setSession(HttpSession session, String mbrId) {
		sessionInfo.put(session, mbrId);
	}

	/**
	 * @기능설명 현재 접속자수 조회
	 */
    public static int getUserCount(){
    	return sessionInfo.size();
    }

    /** HttpSessionListener **/

	/**
	 * 세션 생성시 자동호출
	 */
	public void sessionCreated(HttpSessionEvent e) {
	}

	/**
	 * 세션 만료시 자동호출
	 * sessionInfo에 만료된 세션 존재시 삭제
	 */
	public void sessionDestroyed(HttpSessionEvent e) {

		if(sessionInfo.containsKey(e.getSession())) {
			sessionInfo.remove(e.getSession());
		}
	}
}
