package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.service.MenuService;
import com.nexgrid.cgsg.admin.vo.DataResponse;
import com.nexgrid.cgsg.admin.vo.MenuInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/menu")
public class MenuController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MenuService menuService;

    @RequestMapping("/getLayoutMenuList")
    public List<MenuInfo> getLayoutMenuList(Principal principal) {
        return menuService.getLayoutMenuListByRule(principal.getName());
    }

    @RequestMapping(value = "/getMenuList", method = RequestMethod.POST)
    public DataResponse<MenuInfo> getMenuList(@ModelAttribute MenuInfo menuInfo) {

        List<MenuInfo> mbrList = menuService.getMenuList(menuInfo.getMenuId());

        DataResponse<MenuInfo> dataResponse = new DataResponse<MenuInfo>();
        dataResponse.setData(mbrList);
        dataResponse.setTotal(mbrList.size());

        return dataResponse;
    }

    @RequestMapping(value = "/getRoleMenuList", method = RequestMethod.POST)
    public DataResponse<MenuInfo> getRoleMenuList(@ModelAttribute MenuInfo menuInfo) {

        logger.info("##### 사용중인 메뉴 조회  #####");
        logger.debug("=== getRoleMenuList : " + menuInfo.toString());

        List<MenuInfo> mbrList = null;
        mbrList = menuService.getRoleMenuList();

        DataResponse<MenuInfo> dataResponse = new DataResponse<MenuInfo>();
        dataResponse.setData(mbrList);
        dataResponse.setTotal(mbrList.size());

        return dataResponse;
    }

    @RequestMapping(value = "/getUpMenuList", method = RequestMethod.POST)
    public List<MenuInfo> getUpMenuList(@ModelAttribute MenuInfo menuInfo) {

        logger.info("##### 상위메뉴 조회  #####");
        logger.debug("=== getUpMenuList");

        return menuService.getUpMenuList();
    }

    @RequestMapping(value = "/getMenuInfo", method = RequestMethod.POST)
    public MenuInfo getMenuInfo(@ModelAttribute MenuInfo menuInfo) {

        logger.info("##### 메뉴 정보 조회  #####");
        logger.debug("=== getMenuInfo");

        return  menuService.getMenuInfo(menuInfo.getMenuId());
    }

    @RequestMapping(value = "/setMenuAdd", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
    public int setMenuAdd(@ModelAttribute MenuInfo menuInfo) {

        logger.info("##### 메뉴 등록  #####");
        logger.debug("=== setMenuAdd : " + menuInfo.toString());

        return menuService.setMenuInfo(menuInfo);
    }

    @RequestMapping(value = "/setMenuUpd", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
    public int setMenuUpd(@ModelAttribute MenuInfo menuInfo) {

        logger.info("##### 메뉴 수정  #####");
        logger.debug("=== setMenuUpd : " + menuInfo.toString());

        return menuService.setMenuUpdate(menuInfo);
    }

    @RequestMapping(value = "/setUpMenuAdd", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
    public int setUpMenuAdd(@ModelAttribute MenuInfo menuInfo) {

        logger.info("##### 상위메뉴 등록  #####");
        logger.debug("=== setUpMenuAdd : " + menuInfo.toString());

        return menuService.setUpMenuAdd(menuInfo);
    }
}
