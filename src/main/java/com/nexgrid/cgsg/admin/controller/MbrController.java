package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.security.AdminUser;
import com.nexgrid.cgsg.admin.service.MbrService;
import com.nexgrid.cgsg.admin.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResultInfo getMemberList(Principal principal) {
        if(principal == null) throw new AdminException(SystemStatusCode.INVALID_PARAMETER, "require Login");

        LoginInfo loginInfo  = ((AdminUser)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getLoginInfo();
        List<MbrInfo> memberList = mbrService.getMemberList(loginInfo.getManagerYn(), loginInfo.getMbrId());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(memberList)
                .build();
    }

    @RequestMapping("/getCompanyList")
    public ResultInfo getCompanyList() {
        List<String> companyList = mbrService.getCompanyList();

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(companyList)
                .build();
    }


    @RequestMapping("/insertMbr")
    public ResultInfo insertMbr(@RequestBody @Validated MbrInfo mbrInfo) {
        int insertCnt = mbrService.insertMbr(mbrInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(String.format("%s 건 입력되었습니다.", insertCnt))
                .build();
    }

    @RequestMapping("isDuplicateMember")
    public ResultInfo isDuplicateMember(@RequestBody @Validated DuplicateMbrInfo mbrInfo) {
        boolean duplicateMember = mbrService.isDuplicateMember(mbrInfo.getMbrId(), mbrInfo.getTel());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(duplicateMember)
                .build();
    }

    @RequestMapping("/updateMbr")
    public ResultInfo updateMbr(@RequestBody @Validated MbrInfo mbrInfo) {

        int updateCnt = mbrService.updateMbr(mbrInfo);
        String message = String.format("%s 건 업데이트 되었습니다.", updateCnt);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(message)
                .build();
    }

    @RequestMapping("/updatePwd")
    public ResultInfo updatePwd(@RequestBody @Validated UpdatePwdParam mbrInfo) {
        int updateCnt = mbrService.updatePwd(mbrInfo.getMbrId(), mbrInfo.getMbrNewPw());
        String message = String.format("%s 건 업데이트 되었습니다.", updateCnt);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(message)
                .build();
    }

    @RequestMapping("/deleteMbr")
    public ResultInfo deleteMbr(@RequestBody @Validated DeleteMbrParam mbrInfo) {
        int deleteCnt = mbrService.deleteMbr(mbrInfo.getMbrIdList());
        String message = String.format("%s 건 삭제 되었습니다.", deleteCnt);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(message)
                .build();
    }
}
