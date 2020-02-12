package com.nexgrid.cgsg.admin.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithUserDetails("admin")
public abstract class BaseControllerTest {

    protected final static String NULL_MSG = "없습니다";
    protected final static String OVER_SIZE_MSG = "큽니다";

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Before
    public void baseSetup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
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

    protected String genStr(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }
}
