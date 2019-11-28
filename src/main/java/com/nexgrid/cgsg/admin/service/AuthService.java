package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.constants.UseYnCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.mapper.AuthMapper;
import com.nexgrid.cgsg.admin.mapper.MbrMapper;
import com.nexgrid.cgsg.admin.vo.AuthInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "authService")
public class AuthService {

	@Autowired
	AuthMapper authMapper;

	@Autowired
	MbrMapper mbrMapper;

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

		if(authInfo.getUseYn().equalsIgnoreCase(UseYnCode.N.name()))
			checkUsingRoleCodeInMbrList(authInfo.getRoleCode());

		return authMapper.updateRoleMst(authInfo);
	}

	public boolean isDuplicateRoleCode(String roleCode) {
		Assert.hasLength(roleCode, "roleCode is null");

		int duplicateRoleCode = authMapper.isDuplicateRoleCode(roleCode);

		return duplicateRoleCode > 0;
	}

	public void checkUsingRoleCodeInMbrList(String roleCode) {
		List<MbrInfo> mbrInfoList = mbrMapper.getMemberList("Y", "");

		String findUsers = mbrInfoList.stream()
										   .filter(mbrInfo -> mbrInfo.getRoleCd()
																	 .equalsIgnoreCase(roleCode))
										   .map(mbrInfo -> mbrInfo.getMbrId())
										   .collect(Collectors.joining(","));

		if(StringUtils.isNotEmpty(findUsers))
			throw new AdminException(SystemStatusCode.INVALID_PARAMETER, String.format("[%s] 사용자가 %s 코드를 사용하고 있습니다. 권한 변경 후 미사용으로 변경 해 주세요", findUsers, roleCode));

	}
	
}
