package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MbrInfo {

	@Size(max = 10, message = "mbrId의 크기가 10보다 큽니다")
	@NotNull(message = "mbrId의 값이 없습니다.")
	private String mbrId;

	@Size(max = 512, message = "mbrPw의 크기가 512보다 큽니다")
	private String mbrPw;

	@Size(max = 40, message = "mbrNm의 크기가 40보다 큽니다")
	private String mbrNm;

	@Size(max = 250, message = "email의 크기가 250보다 큽니다")
	@Email(message = "email 정보가 유효하지 않습니다.")
	private String email;

	@Size(max = 20, message = "전화번호의 크기가 20보다 큽니다")
	private String tel;

	@Size(max = 10, message = "role Code의 크기가 10보다 큽니다")
	private String roleCd;

	private String loginEndDt;

	@Size(max = 1, message = "사용유뮤의 크기가 1보다 큽니다")
	private String useYn;

	private String loginFailCnt;

	private String applyDt;
	private String codeNm;

	@Size(max = 512, message = "mbrPw의 크기가 512보다 큽니다")
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
