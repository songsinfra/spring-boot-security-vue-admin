package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.vo.AuthInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest extends BaseServiceTest {

    @Autowired
    private AuthService authService;


    @Test
    public void selectRoleMst_useYn_Y() {
        String useYn = "Y";
        List<AuthInfo> authInfos = authService.selectRoleMstList(useYn);
        assertThat(authInfos).size().isGreaterThan(0);
    }

    @Test
    public void selectRoleMst_useYn_N() {
        String useYn = "N";
        authService.selectRoleMstList(useYn);
    }

    @Test
    public void selectRoleMst_useYn_NULL() {
        String useYn = null;
        authService.selectRoleMstList(useYn);
    }

    @Test
    public void selectRoleMst_useYn_empty() {
        String useYn = "";
        authService.selectRoleMstList(useYn);
    }

    @Test
    public void selectRoleMenuList_rolecode에_해당하는_메뉴가져오기() {
        String roleCode = "R-001";
        String menuId = "";
        List<AuthInfo> authInfos = authService.selectRoleMenuList(roleCode, menuId);

        assertThat(authInfos).size().isGreaterThan(0);
    }

    @Test
    public void selectRoleMenuListByRoleCodeAndMenuId() {
        String roleCode = "R-001";
        String menuId = "AA01";
        List<AuthInfo> authInfos = authService.selectRoleMenuList(roleCode, menuId);

        assertThat(authInfos).size().isEqualTo(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectRoleMenuListByRoleCode_Null() {
        String roleCode = null;
        String menuId = "";

        authService.selectRoleMenuList(roleCode, menuId);
    }

    @Test
    @Transactional
    public void insertRoleMenuList() {
        String roleCode = "R-001";
        List<String> menuIdList = new ArrayList<>();
        menuIdList.add("menu1");
        menuIdList.add("menu2");
        menuIdList.add("menu3");

        int insertCnt = authService.insertRoleMenuList(roleCode, menuIdList);

        assertThat(insertCnt).isEqualTo(menuIdList.size());
    }

    @Test(expected = IllegalArgumentException.class)

    public void insertRoleMenuList_roleCode_없음() {
        String roleCode = "";
        List<String> menuIdList = new ArrayList<>();

        authService.insertRoleMenuList(roleCode, menuIdList);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void insertRoleMenuList_authInfoList_null() {
        String roleCode = "R-001";
        List<String> menuIdList = null;

        authService.insertRoleMenuList(roleCode, menuIdList);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void insertRoleMenuList_authInfoList_empty() {
        String roleCode = "R-001";
        List<String> menuIdList = new ArrayList<>();
        menuIdList.add("");

        authService.insertRoleMenuList(roleCode, menuIdList);
    }

    @Test
    @Transactional
    public void insertRoleMst() {
        AuthInfo authInfo = AuthInfo.builder().roleCode("R-999")
                .managerYn("Y")
                .useYn("Y")
                .codeNm("테스트관리자")
                .build();

        int insertCnt = authService.insertRoleMst(authInfo);

        assertThat(insertCnt).isEqualTo(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertRoleMst_authInfo_Null() {
        AuthInfo authInfo = null;

        authService.insertRoleMst(authInfo);
    }

    @Test
    @Transactional
    public void updateRoleMst() {
        AuthInfo authInfo = AuthInfo.builder().roleCode("R-001")
                .useYn("Y")
                .codeNm("manager")
                .build();

        int updateCnt = authService.updateRoleMst(authInfo);

        assertThat(updateCnt).isEqualTo(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateRoleMst_authInfo_Null() {
        AuthInfo authInfo = null;

        authService.updateRoleMst(authInfo);
    }

    @Test
    public void isDuplicateRoleCode() {
        String roleCode = "R-001";
        boolean duplicateRoleCode = authService.isDuplicateRoleCode(roleCode);

        assertThat(duplicateRoleCode).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void isDuplicateRoleCode_roleCode_null() {
        String roleCode = null;
        authService.isDuplicateRoleCode(roleCode);
    }
}