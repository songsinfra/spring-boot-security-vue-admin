package com.nexgrid.cgsg.admin.vo;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "mbrId")
public class LoginInfo {

	private String mbrId;
	private String mbrNm;
	private String email;
	private String tel;
	private String roleCd;
	private String loginEndDt;
	private String useYn;
	private String loginFailCnt;
	private String applyDt;
	private String managerYn;
	private String mbrPw;
	private String loginFailDt;

	private List<?> subMenuList;	// 하위메뉴 목록
	private List<?> commonMenuList;	// 공통메뉴 목록
}

