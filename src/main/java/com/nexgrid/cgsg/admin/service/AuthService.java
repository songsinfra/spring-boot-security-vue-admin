package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.AuthMapper;
import com.nexgrid.cgsg.admin.vo.AuthInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service(value = "authService")
public class AuthService {

	@Autowired
	AuthMapper authMapper;

	public List<AuthInfo> selectRoleMstList(String useYn) {
		return authMapper.selectRoleMstList(useYn);
	}

	public List<AuthInfo> selectRoleMenuList(String roleCode, String menuId) {
		Assert.hasLength(roleCode, "roleCode is null");

		return authMapper.selectRoleMenuList(roleCode, menuId);
	}

	@Transactional
	public int insertRoleMenuList(String roleCode, List<String> menuIdList) {
		Assert.hasLength(roleCode, "roleCode is null");
		Assert.notNull(menuIdList, "menuIdList is null");

		int insertCnt = 0;

		//해당 권한코드 roleCode 전체 delete
		authMapper.deleteRoleMenu(roleCode);

		for (String menuId : menuIdList) {
			Assert.hasLength(menuId, "menuId is empty");

			insertCnt += authMapper.insertRoleMenu(roleCode, menuId);
		}

		return insertCnt;
	}

	public int insertRoleMst(AuthInfo authInfo) {
		Assert.notNull(authInfo, "authInfo is null");
		Assert.hasLength(authInfo.getRoleCode(), "roleCode is null");
		if(StringUtils.isEmpty(authInfo.getManagerYn())) authInfo.setManagerYn("N");

		return authMapper.insertRoleMst(authInfo);
	}

	public int updateRoleMst(AuthInfo authInfo) {
		Assert.notNull(authInfo, "authInfo is null");
		Assert.hasLength(authInfo.getRoleCode(), "roleCode is null");

		return authMapper.updateRoleMst(authInfo);
	}

	public boolean isDuplicateRoleCode(String roleCode) {
		Assert.hasLength(roleCode, "roleCode is null");

		int duplicateRoleCode = authMapper.isDuplicateRoleCode(roleCode);

		return duplicateRoleCode > 0;
	}
	
}
