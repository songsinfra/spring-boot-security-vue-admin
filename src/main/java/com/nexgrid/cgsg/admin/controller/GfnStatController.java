package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.service.GfnStatService;
import com.nexgrid.cgsg.admin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping("/stat")
public class GfnStatController {
    @Autowired
    private GfnStatService gfnStatService;

    @RequestMapping("/selectUserDetailStat")
    public ResultInfo selectUserStat(@RequestBody @Validated GfnMasterInfoParam gfnMasterInfo) {

        List<GfnMasterInfo> gfnMasterInfos = gfnStatService.selectUserDetailStat(gfnMasterInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnMasterInfos)
                .build();
    }

    @RequestMapping("/selectJoinUserStat")
    public ResultInfo selectJoinUserStat(@RequestBody @Validated GfnJoinStatInfoParam joinStatInfoParam) {
        List<GfnJoinStatMonthInfo> gfnJoinStatMonthInfos = gfnStatService.selectJoinUserStat(joinStatInfoParam);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnJoinStatMonthInfos)
                .build();
    }

    @RequestMapping("/selectJoinStatDetailList")
    public ResultInfo selectJoinStatDetailList(@RequestBody @Validated GfnJoinStatDetailInfoParam joinStatDetailInfo) {
        List<GfnJoinStatDetailInfo> gfnJoinStatDetailInfos = gfnStatService.selectJoinStatDetailList(joinStatDetailInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(gfnJoinStatDetailInfos)
                .build();
    }


}
