package com.nexgrid.cgsg.admin.mapper;

import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginMapper {

	public List<LoginInfo> getLoginInfo(LoginInfo loginInfo);

	public List<MenuInfo> getCommonMenuList(LoginInfo loginInfo);

	public int setUserLock(LoginInfo loginInfo);

	/*사용자 잠금 초기화*/
	public int setUserUnLockAndResetFailCnt(LoginInfo loginInfo);
	
	public int updateLoginFailCnt(LoginInfo loginInfo);

	public int setLoginFailCntInit(LoginInfo loginInfo);

	public List<LoginInfo> getLoginInfoOnlyId(LoginInfo loginInfo);

	public String getApplyDate(MbrInfo mbrInfo);

	public List<LoginInfo> getLoginInfo2(LoginInfo loginInfo);

	public List<MenuInfo> getSubMenuList2(LoginInfo loginInfo);

	public List<MenuInfo> getAllSubMenuList(LoginInfo loginInfo);
	
	/* 직전3회 비밀번호 조회 */
	public int getOldPwValidate(LoginInfo loginInfo);

	public int updateLoginEndDt(LoginInfo loginInfo);	

}