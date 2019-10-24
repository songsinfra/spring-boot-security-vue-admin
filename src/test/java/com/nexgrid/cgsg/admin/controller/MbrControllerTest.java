package com.nexgrid.cgsg.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexgrid.cgsg.admin.base.BaseControllerTest;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.vo.DeleteMbrParam;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import com.nexgrid.cgsg.admin.vo.UpdatePwdParam;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MbrControllerTest extends BaseControllerTest {

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
    @WithUserDetails("admin1")
    public void getMemberList() throws Exception {
        mvc.perform(post("/mbr/getMemberList"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].mbrId").value("admin1"));
    }

    @Test
    @WithAnonymousUser
    public void getMemberList_로그인_안함() throws Exception {
        mvc.perform(post("/mbr/getMemberList"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
              ;
    }

    @Test
    @Transactional
    public void insertMbr() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("iTest1")
                .roleCd("R-001")
                .mbrPw("password")
                .mbrNm("관리자")
                .tel("01012341234")
                .email("test@test.co.kr")
                .mbrCompany("넥스그리드99")
                .mbrDptmt("컨버젼스팀")
                .build();

        mvc.perform(post("/mbr/insertMbr")
                    .content(objectMapper.writeValueAsString(mbrInfo))
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.LOGIN_SUCCESS.getCode()));
    }


    @Test
    @Transactional
    public void insertMbr_mbrId_길이_초과() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("test1test11")
                .roleCd("R-001")
                .mbrPw("password")
                .mbrNm("관리자")
                .tel("01012341234")
                .email("test@test.co.kr")
                .mbrCompany("넥스그리드99")
                .mbrDptmt("컨버젼스팀")
                .build();

        mvc.perform(post("/mbr/insertMbr")
                .content(objectMapper.writeValueAsString(mbrInfo))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void insertMbr_mbrId_NULL() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .roleCd("R-001")
                .mbrPw("password")
                .mbrNm("관리자")
                .tel("01012341234")
                .email("test@test.co.kr")
                .mbrCompany("넥스그리드99")
                .mbrDptmt("컨버젼스팀")
                .build();

        mvc.perform(post("/mbr/insertMbr")
                .content(objectMapper.writeValueAsString(mbrInfo))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void insertMbr_roleCd_길이초과() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("test1")
                .roleCd("R-001R-0012")
                .mbrPw("password")
                .mbrNm("관리자")
                .tel("01012341234")
                .email("test@test.co.kr")
                .mbrCompany("넥스그리드99")
                .mbrDptmt("컨버젼스팀")
                .build();

        mvc.perform(post("/mbr/insertMbr")
                .content(objectMapper.writeValueAsString(mbrInfo))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void isDuplicateMember_mbrId_검색() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("admin1")
                .build();

        mvc.perform(post("/mbr/isDuplicateMember")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("true"));
    }

    @Test
    @Transactional
    public void isDuplicateMember_tel_검색() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .tel("01012341234")
                .build();

        mvc.perform(post("/mbr/isDuplicateMember")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("true"));
    }


    @Test
    @Transactional
    public void isDuplicateMember_조회데이터_없음() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .build();

        mvc.perform(post("/mbr/isDuplicateMember")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbrInfo))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void updateMbr_등록된_ID가_없어_업데이트_안됨() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("null")
                .roleCd("R-001")
                .mbrPw("password")
                .mbrNm("관리자")
                .tel("01012341234")
                .email("test@test.co.kr")
                .mbrCompany("넥스그리드99")
                .mbrDptmt("컨버젼스팀")
                .build();

        mvc.perform(post("/mbr/updateMbr")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.LOGIN_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value("0 건 업데이트 되었습니다."));
    }

    @Test
    @Transactional
    public void updateMbr_비밀번호변경_아이디없음() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("null")
                .roleCd("R-001")
                .mbrPw("password")
                .newPw("password")
                .mbrNm("관리자")
                .tel("01012341234")
                .email("test@test.co.kr")
                .mbrCompany("넥스그리드99")
                .mbrDptmt("컨버젼스팀")
                .build();

        mvc.perform(post("/mbr/updateMbr")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbrInfo))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void updateMbr_비밀번호_변경() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("admin1")
                .roleCd("R-001")
                .newPw("password")
                .mbrNm("관리자")
                .tel("01012341234")
                .email("test@test.co.kr")
                .mbrCompany("넥스그리드99")
                .mbrDptmt("컨버젼스팀")
                .build();

        mvc.perform(post("/mbr/updateMbr")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.LOGIN_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value("1 건 업데이트 되었습니다."));
    }

    @Test
    @Transactional
    public void updateMbr() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("admin1")
                .roleCd("R-001")
                .mbrNm("관리자")
                .tel("01012341234")
                .email("test@test.co.kr")
                .mbrCompany("넥스그리드99")
                .mbrDptmt("컨버젼스팀")
                .build();

        mvc.perform(post("/mbr/updateMbr")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mbrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.LOGIN_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value("1 건 업데이트 되었습니다."));
    }

    @Test
    @Transactional
    public void updatePwd() throws Exception {
        UpdatePwdParam pwdInfo = UpdatePwdParam.builder()
                .mbrId("admin1")
                .mbrNewPw("newPwd")
                .build();

        mvc.perform(post("/mbr/updatePwd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pwdInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.LOGIN_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value("1 건 업데이트 되었습니다."));
    }

    @Test
    @Transactional
    public void updatePwd_mbrId_없음() throws Exception {
        UpdatePwdParam pwdInfo = UpdatePwdParam.builder()
                .mbrNewPw("newPwd")
                .build();

        mvc.perform(post("/mbr/updatePwd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pwdInfo))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void updatePwd_newPwd_없음() throws Exception {
        UpdatePwdParam pwdInfo = UpdatePwdParam.builder()
                .mbrId("admin1")
                .build();

        mvc.perform(post("/mbr/updatePwd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pwdInfo))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void updatePwd_등록이_안된_mbrId_PWD_변경() throws Exception {
        UpdatePwdParam pwdInfo = UpdatePwdParam.builder()
                .mbrId("null")
                .mbrNewPw("newPwd")
                .build();

        mvc.perform(post("/mbr/updatePwd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pwdInfo))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()));
    }

    @Test
    @Transactional
    public void deleteMbr() throws Exception {
        DeleteMbrParam deleteMbrParam = DeleteMbrParam.builder()
                .mbrIdList(Arrays.asList("admin1","admin2"))
                .build();

        mvc.perform(post("/mbr/deleteMbr")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteMbrParam))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

}
