package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.service.GfnPromoService;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfo;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfoParam;
import com.nexgrid.cgsg.admin.vo.ResultInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/side")
public class GfnPromoController {

    @Autowired
    private GfnPromoService promoService;

    @RequestMapping("/insertPromo")
    public ResultInfo insertPromo(@RequestBody @Validated GfnPromoInfo promoInfo, Principal principal) {
        promoInfo.setMbrId(principal.getName());
        String promoCode = promoService.insertPromo(promoInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(promoCode)
                .build();
    }

    @RequestMapping("/selectPromoList")
    public ResultInfo selectPromoList(@RequestBody @Validated GfnPromoInfoParam promoInfo) {
        List<GfnPromoInfo> gfnPromoInfos = promoService.selectPromoList(promoInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnPromoInfos)
                .build();
    }

    @RequestMapping("/existPromoUserInfo")
    public ResultInfo existPromoUserInfo(@RequestBody @Validated GfnPromoInfoParam promoInfo) {
        Assert.notNull(promoInfo, "promoInfo is null");
        Assert.isTrue(ObjectUtils.anyNotNull(promoInfo.getEmail(), promoInfo.getContactNo()), "email or contactNo is require");

        boolean result = promoService.existPromoUserInfo(promoInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(result)
                .build();
    }


    @RequestMapping("/updatePromo")
    public ResultInfo updatePromo(@RequestBody @Validated GfnPromoInfoParam promoInfo, Principal principal) {
        promoInfo.setMbrId(principal.getName());
        int gfnPromoInfos = promoService.updatePromo(promoInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnPromoInfos)
                .build();
    }

    @RequestMapping("/deletePromoCode")
    public ResultInfo deletePromoCode(@RequestBody @Validated GfnPromoInfoParam promoInfo, Principal principal) {

        int result = promoService.deletePromoCode(principal.getName(), promoInfo.getPromoCodeList());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(result + "건 만료처리 되었습니다.")
                .build();
    }



}
