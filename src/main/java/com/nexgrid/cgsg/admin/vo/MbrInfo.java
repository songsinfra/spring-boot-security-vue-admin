package com.nexgrid.cgsg.admin.vo;

import lombok.Data;

@Data
public class MbrInfo {

	private String mbrId;
	private String mbrPw;
	private String mbrNm;
	private String email;
	private String tel;
	private String roleCd;
	private String loginEndDt;
	private String useYn;
	private String loginFailCnt;

	private String applyDt;
	private String codeNm;

	private String newPw;
	private String mbrPwOld1;
	private String mbrPwOld2;

	//search
	private String searchMbrId;
	private String searchDisposition;
	private String searchDstNum;
	private String searchAllCheck;	//미사용포함
	private String managerYn;		//관리자 여부
	private String searchMm;
	
	private String iDisplayStart;
	private String remark;
	
	private String pwApplyDt;
	
	private String mbrCompany;
	private String mbrDptmt;
}
