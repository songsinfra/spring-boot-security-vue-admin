package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.AddItemType;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.service.GfnAddService;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnAddInfoParam;
import com.nexgrid.cgsg.admin.vo.ResultInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/add")
@RestController
public class GfnAddController {

    @Autowired
    private GfnAddService gfnAddService;

    @RequestMapping("/selectAddItemList")
    public ResultInfo selectAddItemList(@RequestBody @Validated GfnAddInfoParam gfnAddInfoParam) {
        String statusCd = gfnAddInfoParam.getStatusCd();
        List<GfnAddInfo> gfnAddInfos = gfnAddService.selectAddItemList(statusCd);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnAddInfos)
                .build();
    }

    @RequestMapping("/selectAddItem")
    public ResultInfo selectAddItem(@RequestBody @Validated GfnAddInfoParam gfnAddInfoParam) {
        Assert.hasLength(gfnAddInfoParam.getAddItemCode(), "addItemCode의 값이 없습니다.");
        GfnAddInfo gfnAddInfo = gfnAddService.selectAddItem(gfnAddInfoParam.getAddItemCode());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnAddInfo)
                .build();
    }

    @RequestMapping("/insertAddItem")
    public ResultInfo insertAddItem(@RequestBody @Validated GfnAddInfo gfnAddInfo) {
        if (isAddItemType(gfnAddInfo, AddItemType.GFN)) {
            gfnAddService.insertAddItemForGfn(gfnAddInfo);
        } else if (isAddItemType(gfnAddInfo, AddItemType.UCUBE)) {
            gfnAddService.insertAddItemForUcube(gfnAddInfo);
        } else {
            throw new AdminException(SystemStatusCode.INVALID_PARAMETER, "AddItemType is invalid");
        }

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }

    @RequestMapping("/updateAddItem")
    public ResultInfo updateAddItem(@RequestBody @Validated GfnAddInfo gfnAddInfo) {
        Assert.hasLength(gfnAddInfo.getAddItemCode(), "addItemCode의 값이 없습니다.");

        if (isAddItemType(gfnAddInfo, AddItemType.GFN)) {
            gfnAddService.updateAddItemForGfn(gfnAddInfo);
        } else if (isAddItemType(gfnAddInfo, AddItemType.UCUBE)) {
            gfnAddService.updateAddItemForUcube(gfnAddInfo);
        } else {
            throw new AdminException(SystemStatusCode.INVALID_PARAMETER, "AddItemType is invalid");
        }

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }


    @RequestMapping("/deleteAddItem")
    public ResultInfo deleteAddItem(@RequestBody @Validated GfnAddInfoParam gfnAddInfo) {
        Assert.hasLength(gfnAddInfo.getAddItemCode(), "addItemCode의 값이 없습니다.");

        gfnAddService.deleteAddItem(gfnAddInfo.getAddItemCode());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }

    private boolean isAddItemType(@Validated @RequestBody GfnAddInfo gfnAddInfo, AddItemType gfn) {
        return StringUtils.equalsIgnoreCase(gfn.getType(), gfnAddInfo.getAddItemType());
    }


}
