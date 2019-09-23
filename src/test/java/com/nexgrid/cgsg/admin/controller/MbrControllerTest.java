package com.nexgrid.cgsg.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
public class MbrControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Bean
//    public UserDetailsService getTestUserDetailsService() {
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                String password = "password";
//
//                List<GrantedAuthority> authorityList = new ArrayList<>();
//                authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
//
//                LoginInfo loginInfo = LoginInfo.builder()
//                        .managerYn("Y")
//                        .mbrId("admin1")
//                        .build();
//
//                AdminUser adminUser = new AdminUser(username, password, authorityList, loginInfo);
//
//                return adminUser;
//            }
//        };
//
//    }

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
                .andExpect(jsonPath("$.[0].mbrId").value("admin1"));
    }

    @Test
    public void getMemberList_로그인_안함() throws Exception {
        mvc.perform(post("/mbr/getMemberList"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("require Login"));
    }

    @Test
    @Transactional
    public void insertMbr() throws Exception {
        MbrInfo mbrInfo = MbrInfo.builder()
                .mbrId("test1")
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

}