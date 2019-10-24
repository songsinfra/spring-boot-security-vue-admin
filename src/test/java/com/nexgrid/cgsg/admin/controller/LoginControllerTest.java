package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.base.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest extends BaseControllerTest {


    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

//    @Test
//    public void loginProcessForAuth() throws Exception {
//        Map params = new HashMap();
//        params.put("username", "admin");
//        params.put("password", "test");
//
//        mvc.perform(post("/login")
//                .param("username", "admin")
//                .param("password", "test"))
//                .andDo(print()).andExpect(status().isOk())
//                .andExpect(jsonPath("userInfo").exists())
//        ;
//
//
//
//
//    }

    @Test
    public void getApplyDate() {

        LocalDateTime loginDateTime = LocalDateTime.parse("201909091450", DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        LocalDateTime nowDateTime = LocalDateTime.now();

        long between = ChronoUnit.MINUTES.between(loginDateTime, nowDateTime);

        System.out.println(between);
    }

    @Test
    public void goLoginFinal() {
    }

    @Test
    public void authVuew() {
    }

    @Test
    public void sessionInfo() {
    }

    @Test
    public void logoutProcess2() {
    }

    @Test
    public void loginChk() {
    }

    @Test
    public void oldInfoChk() {
    }
}