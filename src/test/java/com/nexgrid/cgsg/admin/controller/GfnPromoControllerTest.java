package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.base.BaseControllerTest;
import com.nexgrid.cgsg.admin.constants.StatusCode;
import com.nexgrid.cgsg.admin.service.GfnPromoService;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfo;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfoParam;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnPromoControllerTest extends BaseControllerTest {

    @Autowired
    private GfnPromoService promoService;

    private String EMAIL = "test@email.com";
    private String promoCode1;
    private String promoCode2;

    private GfnPromoInfo.GfnPromoInfoBuilder getPromoInfo() {
        return GfnPromoInfo.builder()
                .email("fda@fdas.soc")
                .contactNo("01012341234")
                .dueDt("20191231")
                .mbrId("test1")
                .statusCd(StatusCode.UNUSED.getCode())
                ;
    }

    @Before
    public void setUp() {
        promoCode1 = promoService.insertPromo(getPromoInfo()
                .email(EMAIL)
                .build());

        promoCode2 = promoService.insertPromo(getPromoInfo()
                .email("dumy@mail.com")
                .build());
    }

    @Test
    public void insertPromo() throws Exception {
        GfnPromoInfo promoInfo = getPromoInfo().build();

        mvc.perform(post("/side/insertPromo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promoInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
        ;
    }

    @Test
    public void selectPromoList() throws Exception {
        GfnPromoInfo promoInfo = new GfnPromoInfo();

        mvc.perform(post("/side/selectPromoList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promoInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
        ;
    }

    @Test
    public void existPromo() throws Exception {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder().email(EMAIL).build();

        mvc.perform(post("/side/existPromo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promoInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("true"))
        ;
    }

    @Test
    public void deletePromoCode() throws Exception {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder().promoCodeList(Arrays.asList(promoCode1, promoCode2)).build();

        mvc.perform(post("/side/deletePromoCode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promoInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("2건 변경이 완료 되었습니다."))
        ;
    }

    @Test
    public void existPromo_invalid_Email() throws Exception {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder().email(EMAIL+"!!!").build();

        mvc.perform(post("/side/existPromo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promoInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("false"))
        ;
    }

    @Test
    public void existPromo_변수_없음() throws Exception {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder().build();

        mvc.perform(post("/side/existPromo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promoInfo))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email or contactNo is require"))
        ;
    }

    @Test
    public void updatePromo() throws Exception {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder()
                .promoCode(this.promoCode1)
                .dueDt("20200101")
                .mbrId("test1")
                .statusCd(StatusCode.USED.getCode())
                .build();

        mvc.perform(post("/side/updatePromo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promoInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("1"))
        ;
    }
}