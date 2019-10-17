package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.service.AuthService;
import com.nexgrid.cgsg.admin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping("/selectRoleMstList")
    public ResultInfo selectRoleMst(@RequestBody(required = false) @Validated RoleMstParam roleMstParam) {
        String useYn = roleMstParam != null ? roleMstParam.getUseYn() : null;

        List<AuthInfo> authInfos = authService.selectRoleMstList(useYn);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(authInfos)
                .build()
                ;
    }

    @RequestMapping("/selectRoleMenuList")
    public ResultInfo selectRoleMenuList(@RequestBody @Validated RoleMstParam roleMstParam) {

        List<AuthInfo> authInfos = authService.selectRoleMenuList(roleMstParam.getRoleCode(), roleMstParam.getMenuId());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(authInfos)
                .build()
                ;
    }

    @RequestMapping("/insertRoleMenuList")
    public ResultInfo insertRoleMenuList(@RequestBody @Validated RoleMenuListParam param) {

        int insertCnt = authService.insertRoleMenuList(param.getRoleCode(), param.getMenuIdList());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(String.format("%s 건 매핑되었습니다.", insertCnt))
                .build()
                ;
    }

    @RequestMapping("/insertRoleMst")
    public ResultInfo insertRoleMst(@RequestBody @Validated AuthInfo authInfo) {

        int insertCnt = authService.insertRoleMst(authInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(String.format("%s 건 입력되었습니다.", insertCnt))
                .build()
                ;
    }

    @RequestMapping("/updateRoleMst")
    public ResultInfo updateRoleMst(@RequestBody @Validated AuthInfo authInfo) {

        int insertCnt = authService.updateRoleMst(authInfo);

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .message(String.format("%s 건 수정되었습니다.", insertCnt))
                .build()
                ;
    }

    @RequestMapping("/isDuplicateRoleCode")
    public ResultInfo isDuplicateRoleCode(@RequestBody @Validated DuplicateRoleCodeParam param) {

        Boolean insertCnt = authService.isDuplicateRoleCode(param.getRoleCode());

        return ResultInfo.builder()
                .code(SystemStatusCode.LOGIN_SUCCESS.getCode())
                .data(insertCnt.toString())
                .build()
                ;
    }

}
