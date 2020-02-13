package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.base.BaseControllerTest;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.vo.GfnJoinStatDetailInfoParam;
import com.nexgrid.cgsg.admin.vo.GfnMasterInfoParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnStatControllerTest extends BaseControllerTest {

    @Test
    public void selectUserStat() throws Exception {
        GfnMasterInfoParam gfnMasterInfo = GfnMasterInfoParam.builder()
                .entrItemCode("2222222")
                .addItemCode("Test1234")
                .createStartDt("19801111")
                .createEndDt("")
                .subNo("7")
                .ctn("ctn")
                .build();

        mvc.perform(post("/stat/selectUserDetailStat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnMasterInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
        ;
    }

    @Test
    public void selectUserStat_date_다른날짜포멧() throws Exception {
        GfnMasterInfoParam gfnMasterInfo = GfnMasterInfoParam.builder()
                                                             .entrItemCode("2222222")
                                                             .addItemCode("Test1234")
                                                             .createStartDt("1980-11-23")
                                                             .createEndDt("")
                                                             .subNo("7")
                                                             .ctn("ctn")
                                                             .build();

        mvc.perform(post("/stat/selectUserDetailStat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnMasterInfo))
        )
           .andDo(print())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()))
        ;
    }

    @Test
    public void selectJoinUserStat() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("date", "sssff");

        mvc.perform(post("/stat/selectJoinUserStat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INTERNAL_ERROR.getCode()))
        ;
    }

    @Test
    public void selectJoinStatDetailList() throws Exception {
        GfnJoinStatDetailInfoParam joinStatDetailInfoParam = GfnJoinStatDetailInfoParam.builder()
                .startDt("20191001")
                .endDt("20191030")
                .build();

        mvc.perform(post("/stat/selectJoinStatDetailList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinStatDetailInfoParam))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
        ;
    }

    @Test
    public void selectJoinStatDetailList_statDt_null() throws Exception {
        GfnJoinStatDetailInfoParam joinStatDetailInfoParam = GfnJoinStatDetailInfoParam.builder()
                .startDt("")
                .endDt("20191030")
                .build();

        mvc.perform(post("/stat/selectJoinStatDetailList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinStatDetailInfoParam))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(SystemStatusCode.INVALID_PARAMETER.getCode()))
        ;
    }


    @Test
    public void selectGfnTosList() throws Exception {
        mvc.perform(post("/stat/selectGfnTosList")
                .contentType(MediaType.APPLICATION_JSON)
        )
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.data").isArray())
        ;
    }}
