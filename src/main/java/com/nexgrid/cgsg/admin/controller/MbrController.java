package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.security.AdminUser;
import com.nexgrid.cgsg.admin.service.MbrService;
import com.nexgrid.cgsg.admin.vo.DuplicateMbrInfo;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import com.nexgrid.cgsg.admin.vo.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/mbr")
public class MbrController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MbrService mbrService;

    @RequestMapping("/getMemberList")
    public List<MbrInfo> getMemberList(Principal principal) {
        if(principal == null) throw new AdminException(SystemStatusCode.INVALID_PARAMETER, "require Login");

        LoginInfo loginInfo  = ((AdminUser)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getLoginInfo();
        return mbrService.getMemberList(loginInfo.getManagerYn(), loginInfo.getMbrId());
    }

    @RequestMapping("/insertMbr")
    public ResultInfo insertMbr(@RequestBody @Validated MbrInfo mbrInfo) {
        int insertCnt = mbrService.insertMbr(mbrInfo);

        return ResultInfo.builder().code(SystemStatusCode.LOGIN_SUCCESS.getCode()).build();
    }

    @RequestMapping("isDuplicateMember")
    public ResultInfo isDuplicateMember(@RequestBody @Validated DuplicateMbrInfo mbrInfo) {
        boolean duplicateMember = mbrService.isDuplicateMember(mbrInfo.getMbrId(), mbrInfo.getTel());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(duplicateMember)
                .build();
    }



}
