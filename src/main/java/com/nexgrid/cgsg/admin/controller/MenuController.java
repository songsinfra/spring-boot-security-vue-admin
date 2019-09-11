package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.service.MenuService;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/getLayoutMenuList")
    public List<MenuInfo> getLayoutMenuList(Principal principal) {
        return menuService.getLayoutMenuListByRule(principal.getName());
    }
}
