package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.service.GfnEntrService;
import com.nexgrid.cgsg.admin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/entr")
public class GfnEntrController {

    @Autowired
    private GfnEntrService gfnEntrService;

    @RequestMapping("/selectEntrItem")
    public ResultInfo selectEntrItem(@RequestBody @Validated GfnEntrInfoParam entrInfoParam) {
        Assert.hasLength(entrInfoParam.getEntrItemCode(), "entrItemCode 의 값이 없습니다.");

        GfnEntrInfo gfnEntrInfo = gfnEntrService.selectEntrItem(entrInfoParam.getEntrItemCode());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnEntrInfo)
                .build();
    }

    @RequestMapping("/selectEntrItemList")
    public ResultInfo selectEntrItemList(@RequestBody @Validated GfnEntrInfoParam entrInfoParam) {
        List<GfnEntrInfo> gfnEntrInfoList = gfnEntrService.selectEntrItemList(entrInfoParam.getEntrItemNm());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnEntrInfoList)
                .build();
    }

    @RequestMapping("/insertEntrItem")
    public ResultInfo insertEntrItem(@RequestBody @Validated GfnEntrInfo gfnEntrInfo) {
        gfnEntrService.insertEntrItem(gfnEntrInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }

    @RequestMapping("/updateEntrItem")
    public ResultInfo updateEntrItem(@RequestBody @Validated GfnEntrInfo gfnEntrInfo) {
        gfnEntrService.updateEntrItem(gfnEntrInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }

    @RequestMapping("/deleteEntrItem")
    public ResultInfo deleteEntrItem(@RequestBody @Validated GfnEntrInfoParam gfnEntrInfo, Principal principal) {
        Assert.hasLength(gfnEntrInfo.getEntrItemCode(), "entrItemCode 의 값이 없습니다.");

        gfnEntrService.deleteEntrItem(gfnEntrInfo.getEntrItemCode(), principal.getName());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }

    @RequestMapping("/selectAddItemListWithMap")
    public ResultInfo selectAddItemListWithMap(@RequestBody @Validated GfnMapInfoParam gfnEntrInfoParam) {
        Assert.hasLength(gfnEntrInfoParam.getEntrItemCode(), "entrItemCode 의 값이 없습니다.");

        List<GfnAddInfo> gfnAddInfos = gfnEntrService.selectAddItemListWithMap(gfnEntrInfoParam.getEntrItemCode());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnAddInfos)
                .build();
    }

    @RequestMapping("/insertMapItem")
    public ResultInfo insertMapItem(@RequestBody @Validated GfnMapInfo gfnMapInfo) {
        gfnEntrService.insertMapItem(gfnMapInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }

    @RequestMapping("/deleteMapItem")
    public ResultInfo deleteMapItem(@RequestBody @Validated GfnMapInfo gfnMapInfo) {
        gfnEntrService.deleteMapItem(gfnMapInfo.getEntrItemCode(), gfnMapInfo.getAddItemCode());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }





}