package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.MenuMapper;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
