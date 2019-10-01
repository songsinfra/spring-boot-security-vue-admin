package com.nexgrid.cgsg.admin.mapper;


import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MbrMapper {

	public List<MbrInfo> getMemberList(String managerYn, String mbrId);

	public List<String> getCompanyList();
	
	public MbrInfo getMbrOldInfo(String mbrId);

	/* id중복체크 */
	public int isDuplicateMember(String mbrId, String tel);

	/*	회원정보 등록 */
	public int insertMbr(MbrInfo mbrInfo);

	public int updateMbr(MbrInfo mbrInfo);
	
	/*회원정보 초기화*/
	public int setMemberPwdInit(MbrInfo mbrInfo);

	public int deleteMbr(List<String> mbrIdList);

}
