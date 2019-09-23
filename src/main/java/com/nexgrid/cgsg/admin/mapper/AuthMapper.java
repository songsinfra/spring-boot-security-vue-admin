package com.nexgrid.cgsg.admin.mapper;

import com.nexgrid.cgsg.admin.vo.AuthInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthMapper {
	
	/**
	 * 등록된 권한 조회
	 */
	List<AuthInfo> selectRoleMst(String useYn);

	/**
	 * 권한에 등록된 메뉴 조회
	 */
	List<AuthInfo> selectRuleMenuList(String roleCode, String menuId);

	/**
	 * 권한에 등록된 메뉴 삭제
	 */
	int deleteRoleMenu(String roleCode);

	/**
	 * 권한에 메뉴등록
	 */
	int insertRoleMenu(String roleCode, String menuId);

	/**
	 * 권한 등록
	 */
	int insertRoleMst(AuthInfo authInfo);

	/**
	 * 권한 수정
	 */
	int updateRoleMst(AuthInfo authInfo);

	/**
	 * 권한코드 중복체크
	 */
	int isDuplicateRoleCode(String  roleCode);

}
