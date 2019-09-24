package com.nexgrid.cgsg.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.vo.AuthInfo;
import com.nexgrid.cgsg.admin.vo.DuplicateRoleCodeParam;
import com.nexgrid.cgsg.admin.vo.RoleMenuListParam;
import com.nexgrid.cgsg.admin.vo.RoleMstParam;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void selectRoleMst_useYn_Y() throws Exception {
        RoleMstParam roleMstParam = RoleMstParam.builder()
                .useYn("Y")
                .build();

        mvc.perform(post("/auth/selectRoleMstList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleMstParam))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void selectRoleMst_useYn_null() throws Exception {
        RoleMstParam roleMstParam = RoleMstParam.builder()
                .build();

        mvc.perform(post("/auth/selectRoleMstList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleMstParam))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void selectRoleMst_useYn_큰_값() throws Exception {
        RoleMstParam roleMstParam = RoleMstParam.builder()
                .useYn("EE")
                .build();

        mvc.perform(post("/auth/selectRoleMstList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleMstParam))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    public void selectRuleMenuList_roleCode로_검색() throws Exception {
        RoleMstParam roleMstParam = RoleMstParam.builder()
                .roleCode("R-001")
                .build();

        mvc.perform(post("/auth/selectRuleMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleMstParam))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void selectRuleMenuList_menuID_추가하여_검색() throws Exception {
        RoleMstParam roleMstParam = RoleMstParam.builder()
                .roleCode("R-001")
                .menuId("AA02")
                .build();

        mvc.perform(post("/auth/selectRuleMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleMstParam))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void selectRuleMenuList_roleCode_null() throws Exception {
        RoleMstParam roleMstParam = RoleMstParam.builder()
                .menuId("AA02")
                .build();

        mvc.perform(post("/auth/selectRuleMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleMstParam))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void insertRoleMenuList() throws Exception {
        RoleMenuListParam param = RoleMenuListParam.builder()
                .roleCode("roleCode")
                .menuIdList(Arrays.asList("menu1","menu2", "menu3"))
                .build();

        mvc.perform(post("/auth/insertRoleMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.LOGIN_SUCCESS.getCode()));
    }

    @Test
    public void insertRoleMenuList_roleCode_null() throws Exception {
        RoleMenuListParam param = RoleMenuListParam.builder()
                .menuIdList(Arrays.asList("menu1","menu2", "menu3"))
                .build();

        mvc.perform(post("/auth/insertRoleMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    public void insertRoleMenuList_menuIdList_null() throws Exception {
        RoleMenuListParam param = RoleMenuListParam.builder()
                .roleCode("roleCode")
                .build();

        mvc.perform(post("/auth/insertRoleMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    public void insertRoleMenuList_menuIdList_empty() throws Exception {
        RoleMenuListParam param = RoleMenuListParam.builder()
                .roleCode("roleCode")
                .menuIdList(Arrays.asList(""))
                .build();

        mvc.perform(post("/auth/insertRoleMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(param))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    private AuthInfo.AuthInfoBuilder getAuthInfoParam() {
        return AuthInfo.builder()
                .roleCode("roleCode")
                .managerYn("Y")
                .useYn("Y")
                .codeNm("테스트관리자1")
                ;
    }

    @Test
    @Transactional
    public void insertRoleMst() throws Exception {
        AuthInfo authInfo = this.getAuthInfoParam().build();

        mvc.perform(post("/auth/insertRoleMst")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.LOGIN_SUCCESS.getCode()));
    }

    @Test
    @Transactional
    public void insertRoleMst_AuthInfo_변수체크() throws Exception {
        String URL = "/auth/insertRoleMst";
        String NULL_MSG = "없습니다";
        String OVER_SIZE_MSG = "큽니다";

        this.checkParam(URL, this.getAuthInfoParam().roleCode(null).build(), NULL_MSG);
        this.checkParam(URL, this.getAuthInfoParam().roleCode(genStr(11)).build(), OVER_SIZE_MSG);

        this.checkParam(URL, this.getAuthInfoParam().managerYn(null).build(), "null");
        this.checkParam(URL, this.getAuthInfoParam().managerYn(genStr(3)).build(), OVER_SIZE_MSG);

        this.checkParam(URL, this.getAuthInfoParam().useYn(null).build(), NULL_MSG);
        this.checkParam(URL, this.getAuthInfoParam().useYn(genStr(2)).build(), OVER_SIZE_MSG);

        this.checkParam(URL, this.getAuthInfoParam().codeNm(genStr(31)).build(), OVER_SIZE_MSG);
    }

    @Test
    @Transactional
    public void updateRoleMst() throws Exception {
        AuthInfo authInfo = this.getAuthInfoParam().build();

        this.successRequestAndCheckMsg("/auth/updateRoleMst", authInfo);
    }

    @Test
    @Transactional
    public void updateRoleMst_AuthInfo_변수체크() throws Exception {
        String URL = "/auth/updateRoleMst";
        String NULL_MSG = "없습니다";
        String OVER_SIZE_MSG = "큽니다";

        this.checkParam(URL, this.getAuthInfoParam().roleCode(null).build(), NULL_MSG);
        this.checkParam(URL, this.getAuthInfoParam().roleCode(genStr(11)).build(), OVER_SIZE_MSG);

        this.checkParam(URL, this.getAuthInfoParam().useYn(null).build(), NULL_MSG);
        this.checkParam(URL, this.getAuthInfoParam().useYn(genStr(2)).build(), OVER_SIZE_MSG);

        this.checkParam(URL, this.getAuthInfoParam().codeNm(genStr(31)).build(), OVER_SIZE_MSG);
    }


    @Test
    @Transactional
    public void isDuplicateRoleCode() throws Exception {
        DuplicateRoleCodeParam param = DuplicateRoleCodeParam.builder().roleCode("R-001").build();

        this.successRequestAndCheckMsg("/auth/isDuplicateRoleCode", param, "true");
    }

    @Test
    @Transactional
    public void isDuplicateRoleCode_중복_roleCode_없음() throws Exception {
        DuplicateRoleCodeParam param = DuplicateRoleCodeParam.builder().roleCode("roleCode").build();

        this.successRequestAndCheckMsg("/auth/isDuplicateRoleCode", param, "false");
    }

    @Test
    @Transactional
    public void isDuplicateRoleCode_DuplicateRoleCodeParam_변수체크() throws Exception {
        String URL = "/auth/updateRoleMst";
        String NULL_MSG = "없습니다";
        String OVER_SIZE_MSG = "큽니다";

        this.checkParam(URL, DuplicateRoleCodeParam.builder().roleCode(null).build(), NULL_MSG);
        this.checkParam(URL, DuplicateRoleCodeParam.builder().roleCode(genStr(11)).build(), OVER_SIZE_MSG);
    }


    private String genStr(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    public ResultActions successRequestAndCheckMsg(String url, Object model) throws Exception {
        return  mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(model))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.LOGIN_SUCCESS.getCode()));
    }

    public void successRequestAndCheckMsg(String url, Object model, String containStrForData) throws Exception {
        ResultActions resultActions = this.successRequestAndCheckMsg(url, model);

        if (!StringUtils.isEmpty(containStrForData)) {
            resultActions
                    .andExpect(jsonPath("$.data").value(containsString(containStrForData)));
        }
    }

    public void checkParam(String url, Object model, String containStr) throws Exception {
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(model))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()))
                .andExpect(jsonPath("$.message").value(containsString(containStr)))
        ;
    }

}