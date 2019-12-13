package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.security.AdminUser;
import com.nexgrid.cgsg.admin.service.LoginService;
import com.nexgrid.cgsg.admin.service.MbrService;
import com.nexgrid.cgsg.admin.utils.SessionUtil;
import com.nexgrid.cgsg.admin.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/mbr")
public class MbrController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MbrService mbrService;

    @Autowired
    private LoginService loginService;

    @RequestMapping("/whois")
    public ResultInfo whois(Principal principal) {
        List<LoginInfo> loginInfoList = loginService.getLoginInfo2(LoginInfo.builder().mbrId(principal.getName()).build());
        return ResultInfo.builder()
                .data(extractLoginInfoForFront(loginInfoList.get(0)))
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .build();
    }

    private LoginInfo extractLoginInfoForFront(LoginInfo loginInfo) {
        Assert.notNull(loginInfo, "loginInfo is null");

        return LoginInfo.builder()
                .mbrId(loginInfo.getMbrId())
                .mbrNm(loginInfo.getMbrNm())
                .loginFailCnt(loginInfo.getLoginFailCnt())
                .loginEndDt(loginInfo.getLoginEndDt())
                .email(loginInfo.getEmail())
                .managerYn(loginInfo.getManagerYn())
                .roleCd(loginInfo.getRoleCd())
                .tel(loginInfo.getTel())
                .build();
    }

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

        if (updateCnt > 0 && StringUtils.isNotEmpty(mbrInfo.getNewPw()) && !SessionUtil.isAdmin() ) {
            SecurityContextHolder.clearContext();
        }

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(message)
                .build();
    }

    @RequestMapping("/updatePwd")
    public ResultInfo updatePwd(@RequestBody @Validated UpdatePwdParam mbrInfo) {

        if(loginService.isSamePassword(LoginInfo.builder()
                                                .mbrId(mbrInfo.getMbrId())
                                                .mbrPw(mbrInfo.getMbrNewPw())
                                             .build()))
            throw new AdminException(SystemStatusCode.INVALID_PARAMETER, "기존에 사용하던 비밀번호입니다");


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

    /**
     * @Date : 2015. 9. 7.
     * @작성자 : 김영호
     * @프로그램 설명 : 비밀번호 변경시 기존 비밀번호 확인
     * @개정이력 :
     */
    @RequestMapping(value = "/isExistLoginInfo", method = RequestMethod.POST)
    public ResultInfo loginChk(@RequestBody LoginInfo loginInfo) {

        logger.debug("=== loginChk loginInfo: " + loginInfo.toString());

        boolean result = loginService.isExistLoginInfo(loginInfo);

        return ResultInfo.builder()
                         .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                         .data(result)
                         .build();
    }


    /**
     * @author PSJ
     * @Date   2019. 3. 25.
     * @param
     * @기능설명 비밀번호 변경 시 직전3회 체크
     *
     */
    @RequestMapping(value = "/isSamePassword", method = RequestMethod.POST)
    public ResultInfo oldInfoChk(@RequestBody LoginInfo loginInfo) {

        logger.debug("=== oldInfoChk loginInfo: " + loginInfo.toString());

        boolean result = loginService.isSamePassword(loginInfo);

        return ResultInfo.builder()
                         .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                         .data(result)
                         .build();
    }
}
