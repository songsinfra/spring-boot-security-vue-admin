package com.nexgrid.cgsg.admin.vo;

import com.nexgrid.cgsg.admin.utils.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfo {
	private String roleCode;
	private String codeNm;
	private String managerYn;
	private String useYn;	
	private String menuId;	

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getRoleCode() {
		return CommonUtil.replaceSeparator(roleCode);
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getCodeNm() {
		return CommonUtil.replaceSeparator(codeNm);
	}

	public void setCodeNm(String codeNm) {
		this.codeNm = codeNm;
	}

	public String getManagerYn() {
		return managerYn;
	}

	public void setManagerYn(String managerYn) {
		this.managerYn = managerYn;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}


}
