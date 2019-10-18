package com.nexgrid.cgsg.admin.utils;

import nexgrid_SHA512.Nexgrid_SHA512;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class CommonUtil {
	private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	/**
	 * null 처리용 메서드
	 * null과 여러개의 공백만 있을 경우도 빈값으로 반환
	 * @author "김종헌(새아소프트)"
	 * @since 2015.07.15.
	 * @param String
	 * @return String
	 */
	public static String nullCheck(String checkString) {
		if (checkString == null || checkString.trim().length() <= 0) {
			return "";
		} else {
			return checkString;
		}
	}

	public static int getNullValue(String checkString) {
		if (checkString == null || checkString.trim().length() <= 0) {
			return 0;
		} else {
			return Integer.parseInt(checkString);
		}
	}

	public static String convertEncryptPassword(String password) {
		return Nexgrid_SHA512.encrypt(password);
	}
	
	/**
	 * 시간의 시 목록 설정
	 * @author "김종헌(새아소프트)"
	 * @since 2015.07.23.
	 * @return String[]
	 */
	public static String[] setHourList() {
		int setInt = 24;
		String[] hourList = new String[setInt];
		for(int i = 0 ; i < setInt ; i++) {
			if(i < 10) {
				hourList[i] = "0" + i;
			} else {
				hourList[i] = String.valueOf(i);
			}
		}
		return hourList;
	}
	
	/**
	 * 시간의 분 목록 설정
	 * @author "김종헌(새아소프트)"
	 * @since 2015.07.23.
	 * @return String[]
	 */
	public static String[] setMinuteList() {
		int setInt = 60;
		String[] minuteList = new String[setInt];
		for(int i = 0 ; i < setInt ; i++) {
			if(i < 10) {
				minuteList[i] = "0" + i;
			} else {
				minuteList[i] = String.valueOf(i);
			}
		}
		return minuteList;
	}
	
	/**
	 * 지정된 포맷으로 날짜, 시간 정보 가져오기
	 * @author 이정훈(엔아이링크)
	 * @param form
	 * @param date
	 * @return String
	 */
	public static String getDateTime(String form, Date date){
        return new SimpleDateFormat(form).format( date );
	}
	
	/**
	 * 지정된 포맷으로 현재 날짜, 시간 정보 가져오기
	 * @author 이정훈(엔아이링크)
	 * @param form
	 * @return String
	 */
	public static String getCurrentDateTime(String form){
        return getDateTime( form, new Date() );
	}
	
	/**
	 * 문자열이 숫자인지 검사
	 * @author 이정훈(엔아이링크)
	 * @param str
	 * @return Boolean
	 */
	public static Boolean isNumeric(String str){
		boolean rst = false;
		
		try{
			Integer.parseInt(str);
			rst = true;
		}catch(Exception e){}
		
		return rst;
	}
	
	/**
	 * 특정문자로 문자열 자르기
	 * @author 이정훈(엔아이링크)
	 * @param haystack
	 * @param needle
	 * @param beforeNeedle
	 * @return String
	 */
	public static String strstr(String haystack, String needle, Boolean beforeNeedle){
		
		String rst = null;
		int index = haystack.indexOf(needle);
		
		if( beforeNeedle ){
			rst = haystack.substring(0, index);
		}else{
			rst = haystack.substring(index);
		}
		
		return rst;
	}
	
	/**
	 * 배열이나 리스트를 WHERE IN 문법에 들어가는 내용으로 파싱하여 리턴
	 * @author 이정훈(엔아이링크)
	 * @param strArray
	 * @return String
	 */
	public static String getWhereIn(String[] strArray){
		
		StringBuffer sb = new StringBuffer();

		for(String str : strArray){
			sb.append(str);
			sb.append(",");
		}
		
		return sb.toString().substring(0, sb.toString().length() -1);
	}
	
	/**
	 * 배열이나 리스트를 WHERE IN 문법에 들어가는 내용으로 파싱하여 리턴
	 * @author 이정훈(엔아이링크)
	 * @param strArray
	 * @return String
	 */
	public static String getWhereIn(List<String> strList){
		
		StringBuffer sb = new StringBuffer();
		
		for(String str : strList){
			sb.append(str);
			sb.append(",");
		}
		
		return sb.toString().substring(0, -1);
	}
	
	/**
	 * 숫자를 hh:mm:ss 형식으로 리턴
	 * @author 이정훈(엔아이링크)
	 * @param number
	 * @return String
	 */
	public static String intToTime(int number){
		
		int hour = (int) Math.floor( number / 3600 );
		int min = (int) Math.floor( number / 60 % 60 );
		int sec = (int) Math.floor( number % 60 );

		String sHour = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
		String sMin = min < 10 ? "0" + String.valueOf(min) : String.valueOf(min);
		String sSec = sec < 10 ? "0" + String.valueOf(sec) : String.valueOf(sec);
		
		return sHour+":"+sMin+":"+sSec;
	}
	
	/**
	 * 제로필코드를 복구
	 * @author 이정훈(엔아이링크)
	 * @param String
	 * @return String
	 */
	public static String codeRepair(String codeNum){
		String temp = new String();
		
		for(int i=0;i<=9-codeNum.length();i++){
			temp = "0"+temp;
		}
		
		return temp+codeNum;
	}
	
	/**
	 * 부서코드 3자리수 기준으로 좌측 제로값를 복구
	 * @author 김영호(엔아이링크)
	 * @param String
	 * @return String
	 */	
	public static String zerofillString(String codeNum) {
		if(nullCheck(codeNum).length() < 3) {
			switch(codeNum.length()) {
				case 0 : codeNum = "000";			break;
				case 1 : codeNum = "00"+codeNum;	break;
				case 2 : codeNum = "0"+codeNum;		break;
				default : break;
			}
		}
		return codeNum;
	}	
	
	/**
	 * 특수문자 /, : 제거 ( 결재등록시 파싱데이터 )
	 * @author 김영호
	 * @param String
	 * @return String
	 */	
	public static String replaceApprCmtStr(String str) {
		str = nullCheck(str).replaceAll("/", "").replaceAll(":", "");
		return str;
	}
	
	/**
	 * 자리수에 따른 영문대소문자 숫자 랜덤 반환
	 * @author 김영호
	 * @param Int (자리수)
	 * @return String
	 */	
	public static String getRamdonStr(int len) {
		
		Random rnd =new Random();
		StringBuffer buf =new StringBuffer();
		 
		for(int i=0;i<len;i++){
		    if(rnd.nextBoolean()){
		    	if(rnd.nextBoolean()) {
		    		buf.append((char)((int)(rnd.nextInt(26))+65));//영문대문자
		    	} else {
		    		buf.append((char)((int)(rnd.nextInt(26))+97));//영움소분자
		    	}
		    }else{
		        buf.append((rnd.nextInt(10)));//숫자
		    }
		}
		
		return buf.toString();
	}

	/**
	 * 개행문자(줄바끔) 치환
	 * @author 김영호
	 * @param Int (자리수)
	 * @return String
	 */	
	public static String replaceSeparator(String str) {

		try {
			
			if(str == null ) {
				str = "";
			} else {
				str = str
						.replaceAll("\n"," ")
						.replaceAll("'","")
						.replaceAll("\"","")     // " => 공백
						.replaceAll("<","&lt;")
						.replaceAll(">","&gt;")
						;
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return str;
	}

	/**
	 * 회원가입시 파싱데이터의 비밀번호 암호화 적용
	 * @author 김영호
	 * @param String (DS_APPR_INFO.DRAFTCMT, KEY NM)
	 * @return String
	 */	
	public static String getDraftCmtEncrypt(String draftCmt, String chgStr) {
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("### 회원가입시 파싱데이터 비밀번호 암호화 ###");
		System.out.println("### 원본:"+draftCmt);

		String origin_col = "";
		String new_col = "";

		try {
			String[] arr_draftCmt = draftCmt.split("/");
			
			for(String s : arr_draftCmt) {
				String [] col = s.split(":");
				
				String key = col[0];
				
				switch(key){
					case "MBR_PW" : 
						String value = col[1];
						String enc_pw = Nexgrid_SHA512.encrypt(value);
						origin_col = s;
						new_col = "MBR_PW:"+enc_pw;
						break;
					default : break;
				}
			}
			System.out.println("### 변경할 원본값 :"+origin_col);
			System.out.println("### 변경할 값 :"+new_col);
			System.out.println("### 암호화 수정본:"+draftCmt.replaceAll(origin_col, new_col));
			System.out.println("-----------------------------------------------------------------------");

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return draftCmt.replaceAll(origin_col, new_col);
	}
	
	public static String noXss(String str_low) {
		str_low = str_low.replaceAll("<", "&lt;");
		str_low = str_low.replaceAll(">", "&gt;");
		
		String noXss_str = "";
		noXss_str = str_low.toLowerCase();
		
		if(str_low.contains("javascript") || str_low.contains("script") || str_low.contains("iframe") || str_low.contains("document")
				|| str_low.contains("vbscript")	|| str_low.contains("applet") || str_low.contains("embed") || str_low.contains("object") || str_low.contains("frame") || str_low.contains("grameset")
				 || str_low.contains("layer") || str_low.contains("bgsound") || str_low.contains("alert") || str_low.contains("onblur") || str_low.contains("onchange")
				 || str_low.contains("onclick") || str_low.contains("ondblclick") || str_low.contains("enerror") || str_low.contains("onfocus") || str_low.contains("onload")
				 || str_low.contains("onmouse") || str_low.contains("onscroll") || str_low.contains("onsubmit") || str_low.contains("onunload")		 ) {
			
			str_low = noXss_str;
			str_low = str_low.replace("javascript", "x-javascript");
			str_low = str_low.replace("script", 	"x-script");
			str_low = str_low.replace("iframe", 	"x-iframe");
			str_low = str_low.replace("document", 	"x-document");
			str_low = str_low.replace("vbscript", 	"x-vbscript");
			str_low = str_low.replace("applet", 	"x-applet");
			str_low = str_low.replace("embed", 		"x-embed");
			str_low = str_low.replace("object", 	"x-object");
			str_low = str_low.replace("frame", 		"x-frame");
			str_low = str_low.replace("grameset", 	"x-grameset");
			str_low = str_low.replace("layer", 		"x-layer");
			str_low = str_low.replace("bgsound", 	"x-bgsound");
			str_low = str_low.replace("alert", 		"x-alert");
			str_low = str_low.replace("onblur", 	"x-onblur");
			str_low = str_low.replace("onchange", 	"x-onchange");
			str_low = str_low.replace("onclick", 	"x-onclick");
			str_low = str_low.replace("ondblclick", "x-ondblclick");
			str_low = str_low.replace("enerror", 	"x-enerror");
			str_low = str_low.replace("onfocus", 	"x-onfocus");
			str_low = str_low.replace("onload", 	"x-onload");
			str_low = str_low.replace("onmouse", 	"x-onmouse");
			str_low = str_low.replace("onscroll", 	"x-onscroll");
			str_low = str_low.replace("onsubmit", 	"x-onsubmit");
			str_low = str_low.replace("onunload", 	"x-onunload");
		}
		
		return str_low;
	}	
}
