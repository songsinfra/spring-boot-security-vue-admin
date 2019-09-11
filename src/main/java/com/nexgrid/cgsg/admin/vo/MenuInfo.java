package com.nexgrid.cgsg.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuInfo {

	private String menuId;
	private String menuName;
	private String upMenuId;
	private String upMenuName;
	private String menuURL;
	
	private String ordNo;
	private String useYn;
	private String regper;
	private String regdt;
	private String depth;
	private String modper;
	private String moddate;
	private String orgmenuId;
	private String memId;
	
	private String selYn;
	private String insYn;
	private String uptYn;
	private String delYn;

	/*
	 * search
	 */
	private String searchMenuId;	// 메뉴ID
	private String searchMenuName;	// 메뉴명
	private String searchUseYn;		// 사용여부

	private List<MenuInfo> subMenu;
}