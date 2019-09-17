package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.mapper.MenuMapper;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public List<MenuInfo> getLayoutMenuListByRule(String mbrId) {
        // 공통메뉴 처리 - 세션에 'commonMenuList'명으로 공통메뉴 목록 객체를 저장
        List<MenuInfo> commonMenuList = menuMapper.getRootMenuList(mbrId);
        // 각 사용자 권한에 따른 하위메뉴 목록 - 세션에 'subMenuList'명으로 하위메뉴 목록 객체를 저장
        List<MenuInfo> subMenuList = menuMapper.getSubMenuList(mbrId);

        List<MenuInfo> setedMenuList = commonMenuList.stream().map(menuInfo -> {
            List<MenuInfo> fillteredMenuList = subMenuList.stream().filter(submenu -> submenu.getUpMenuId().equalsIgnoreCase(menuInfo.getMenuId())).collect(Collectors.toList());
            menuInfo.setSubMenu(fillteredMenuList);
            return menuInfo;
        }).collect(Collectors.toList());

        return setedMenuList;
    }

    public List<MenuInfo> getMenuList(String searchUseYn) {
        return menuMapper.getMenuList(searchUseYn);
    }

    public List<MenuInfo> getRoleMenuList() {
        return menuMapper.getRoleMenuList();
    }

    public List<MenuInfo> getUpMenuList() {
        return menuMapper.getUpMenuList();
    }

    public MenuInfo getMenuInfo(String menuId) {
        return menuMapper.getMenuInfo(menuId);
    }

    public int setMenuInfo(MenuInfo menuInfo) {
        Assert.hasLength(menuInfo.getUpMenuId(), "upMenuId is null");

        int menuCnt = menuMapper.getMenuCnt(menuInfo.getUpMenuId());
        menuInfo.setMenuId(menuInfo.getUpMenuId() + (menuCnt < 10 ? "0" + menuCnt : menuCnt));

        return menuMapper.setMenuAdd(menuInfo);
    }

    public int setMenuUpdate(MenuInfo menuInfo) {
        Assert.hasLength(menuInfo.getMenuName(), "MenuName is null");
        Assert.hasLength(menuInfo.getUseYn(), "useYN is null");
        Assert.hasLength(menuInfo.getOrdNo(), "ordNo is null");
        Assert.hasLength(menuInfo.getMenuId(), "menuId is null");

        return menuMapper.setMenuUpdate(menuInfo);
    }


    public int setUpMenuAdd(MenuInfo menuInfo) {
        int upMenuCnt = menuMapper.getUpMenuCnt();

        menuInfo.setMenuId(this.generateMenuId(upMenuCnt));

        return menuMapper.setUpMenuAdd(menuInfo);
    }

    public String generateMenuId(int rowCount) {
        if(rowCount > 675) throw new AdminException(SystemStatusCode.INVALID_PARAMETER, "Invalid rowCount for generating menuId");

        final int ALPHABET_LENGTH = 26;

        char FIRST_CODE = 'A';
        char SECOND_CODE = 'A';

        char firstCode = (char) (FIRST_CODE + (rowCount / ALPHABET_LENGTH));
        char secondCode = (char) (SECOND_CODE + (rowCount % ALPHABET_LENGTH));

        return String.format("%c%c", firstCode, secondCode);
    }

}
