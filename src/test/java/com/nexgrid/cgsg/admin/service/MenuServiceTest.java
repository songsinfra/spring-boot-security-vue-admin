package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.mapper.MenuMapper;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuServiceTest extends BaseServiceTest {

    @Autowired
    private MenuService menuService;

    @Mock
    private MenuMapper menuMapper;

    @InjectMocks
    private MenuService menuServiceMock;

    @Test
    public void getLayoutMenuListByRule() {
        String mbrId = "admin";

        List<MenuInfo> layoutMenuListByRule = menuService.getLayoutMenuListByRule(mbrId);

        assertThat(layoutMenuListByRule).size().isGreaterThan(0);
        assertThat(layoutMenuListByRule.get(0).getSubMenu()).size().isGreaterThan(0);
    }

    @Test
    public void getMenuList_DB() {
        String searchUserYn = "Y";

        List<MenuInfo> menuList = menuService.getMenuList(searchUserYn);

        assertThat(menuList).size().isGreaterThan(0);
    }

    @Test
    public void getRoleMenuList_DB() {
        List<MenuInfo> menuList = menuService.getRoleMenuList();

        assertThat(menuList).size().isGreaterThan(0);
    }

    @Test
    public void getUpMenuList_DB() {
        List<MenuInfo> menuList = menuService.getUpMenuList();

        assertThat(menuList).size().isGreaterThan(0);
    }

    @Test
    public void getGetMenuInfo_DB() {
        String menuId = "AA01";

        MenuInfo menuList = menuService.getMenuInfo(menuId);

        assertThat(menuList).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void setMenuInfo_DB_ERROR_IllegalArg() {
        MenuInfo menuInfo = new MenuInfo();

        int insertCount = menuService.setMenuInfo(menuInfo);
    }

    @Test
    @Transactional
    public void setMenuInfo_DB() {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setUpMenuId("AA");
        menuInfo.setMenuName("TEST메뉴");
        menuInfo.setMenuURL("/test/test");
        menuInfo.setUseYn("Y");

        int insertCount = menuService.setMenuInfo(menuInfo);

        assertThat(insertCount).isEqualTo(1);
    }

    @Test
    public void setMenuInfo_메뉴ID_넘버링_10이상_Mock() {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setUpMenuId("AA");
        menuInfo.setMenuName("TEST메뉴");
        menuInfo.setMenuURL("/test/test");
        menuInfo.setUseYn("Y");

        when(menuMapper.getMenuCnt(any())).thenReturn(10);

        int insertCount = menuServiceMock.setMenuInfo(menuInfo);

        ArgumentCaptor<MenuInfo> menuInfoArgumentCaptor = ArgumentCaptor.forClass(MenuInfo.class);
        verify(menuMapper).setMenuAdd(menuInfoArgumentCaptor.capture());
        assertThat(menuInfoArgumentCaptor.getValue().getMenuId()).isEqualTo("AA10");
    }

    @Test
    public void setMenuInfo_메뉴ID_넘버링_10이하_Mock() {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setUpMenuId("AA");
        menuInfo.setMenuName("TEST메뉴");
        menuInfo.setMenuURL("/test/test");
        menuInfo.setUseYn("Y");

        when(menuMapper.getMenuCnt(any())).thenReturn(9);

        int insertCount = menuServiceMock.setMenuInfo(menuInfo);

        ArgumentCaptor<MenuInfo> menuInfoArgumentCaptor = ArgumentCaptor.forClass(MenuInfo.class);
        verify(menuMapper).setMenuAdd(menuInfoArgumentCaptor.capture());
        assertThat(menuInfoArgumentCaptor.getValue().getMenuId()).isEqualTo("AA09");
    }

    @Test
    @Transactional
    public void setMenuUpdate_메뉴수정() {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setMenuId("AA");
        menuInfo.setMenuName("TEST메뉴");
        menuInfo.setMenuURL("/test/test");
        menuInfo.setOrdNo("1");
        menuInfo.setUseYn("Y");

        int insertCount = menuService.setMenuUpdate(menuInfo);

        assertThat(insertCount).isEqualTo(1);
    }


    @Test
    public void generateMenuId() {
        assertThat(menuService.generateMenuId(0)).isEqualTo("AA");
        assertThat(menuService.generateMenuId(1)).isEqualTo("AB");
        assertThat(menuService.generateMenuId(26)).isEqualTo("BA");
        assertThat(menuService.generateMenuId(27)).isEqualTo("BB");
        assertThat(menuService.generateMenuId(675)).isEqualTo("ZZ");
    }

}