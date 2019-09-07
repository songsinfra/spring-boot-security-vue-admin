package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.LoginMapper;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "loginService")
public class LoginService {
	private Logger log = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private LoginMapper mapper;

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
		return mapper.setUserUnLock(loginInfo);
	}
	
	public int getOldPwValidate(LoginInfo loginInfo) {
		return mapper.getOldPwValidate(loginInfo);
	}

	public int updateLoginEndDt(LoginInfo loginInfo) {
		return mapper.updateLoginEndDt(loginInfo);
	}
}
