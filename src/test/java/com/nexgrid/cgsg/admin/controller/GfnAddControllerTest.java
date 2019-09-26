package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.base.BaseControllerTest;
import com.nexgrid.cgsg.admin.constants.AddItemType;
import com.nexgrid.cgsg.admin.constants.SvcTermType;
import com.nexgrid.cgsg.admin.constants.SvcTermUnit;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnAddInfoParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnAddControllerTest extends BaseControllerTest {


    @Test
    public void selectAddItemList() throws Exception {
        GfnAddInfoParam gfnAddInfoParam = GfnAddInfoParam.builder()
                .addItemNm("")
                .build();

        mvc.perform(post("/add/selectAddItemList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfoParam))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void selectAddItem() throws Exception {
        String addItemCode = "TES00001";
        GfnAddInfo gfnAddInfo = GfnAddInfo.builder().addItemCode(addItemCode).build();

        mvc.perform(post("/add/selectAddItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.addItemCode").value(addItemCode));
    }

    @Test
    public void selectAddItem_param_체크() throws Exception {
        this.checkParam("/add/selectAddItem", GfnAddInfo.builder().build(), this.NULL_MSG);
        this.checkParam("/add/selectAddItem", GfnAddInfo.builder().addItemCode("").build(), this.NULL_MSG);
        this.checkParam("/add/selectAddItem", GfnAddInfo.builder().addItemCode(this.genStr(11)).build(), this.OVER_SIZE_MSG);
    }


    private GfnAddInfo.GfnAddInfoBuilder getInitGfnAddInfoForGfn() {
        return GfnAddInfo.builder()
                .addItemType(AddItemType.GFN.getType())
                .addItemNm("테스트 부과서비스1")
                .svcSellPrice(1000)
                .svcBasePrice(5000)
                .addItemDetail("디테일1")
                .addItemNotice("notice1")
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermUnit(SvcTermUnit.Month.getCode())
                .svcTermNum(1);
    }

    private GfnAddInfo.GfnAddInfoBuilder getInitGfnAddInfoForUcube() {
        return GfnAddInfo.builder()
                .addItemCode("UCUBE00001")
                .addItemType(AddItemType.UCUBE.getType())
                .addItemNm("테스트 부과서비스1")
                .svcSellPrice(1000)
                .svcBasePrice(5000)
                .addItemDetail("디테일1")
                .addItemNotice("notice1")
                .svcTermType(SvcTermType.NONE.getType())
                ;
    }

    @Test
    public void insertAddItem_gfn_AVAILABLE_DATE() throws Exception {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn().build();

        mvc.perform(post("/add/insertAddItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void insertAddItem_gfn_limitDate() throws Exception {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.LIMIT_DATE.getType())
                .svcTermDate(new Date())
                .build();

        mvc.perform(post("/add/insertAddItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void insertAddItem_ucube() throws Exception {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube().build();

        mvc.perform(post("/add/insertAddItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void insertAddItem_GfnAddInfo_잘못된값() throws Exception {
        this.checkParam("/add/insertAddItem", this.getInitGfnAddInfoForGfn().addItemType("X").build(), "AddItemType is invalid");

        this.checkParam("/add/insertAddItem", this.getInitGfnAddInfoForGfn().addItemCode(genStr(11)).build(), this.OVER_SIZE_MSG);
        this.checkParam("/add/insertAddItem", this.getInitGfnAddInfoForGfn().addItemType(null).build(), this.NULL_MSG);
        this.checkParam("/add/insertAddItem", this.getInitGfnAddInfoForGfn().addItemType(genStr(2)).build(), OVER_SIZE_MSG);

        this.checkParam("/add/insertAddItem", this.getInitGfnAddInfoForGfn().addItemNm(genStr(121)).build(), OVER_SIZE_MSG);
    }

    @Test
    public void updateAddItem_gfn_AVAILABLE_DATE() throws Exception {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .addItemCode("TES00001")
                .build();

        mvc.perform(post("/add/updateAddItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void updateAddItem_gfn_limitDate() throws Exception {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .addItemCode("TES00001")
                .svcTermType(SvcTermType.LIMIT_DATE.getType())
                .svcTermDate(new Date())
                .build();

        mvc.perform(post("/add/updateAddItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void updateAddItem_ucube() throws Exception {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemCode("TES00001")
                .build();

        mvc.perform(post("/add/updateAddItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void updateAddItem_GfnAddInfo_잘못된값() throws Exception {
        this.checkParam("/add/updateAddItem", this.getInitGfnAddInfoForUcube().addItemType("X").build(), "AddItemType is invalid");
        this.checkParam("/add/updateAddItem", this.getInitGfnAddInfoForUcube().addItemCode(null).build(), this.NULL_MSG);
    }

    @Test
    public void deleteAddItem() throws Exception {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemCode("TES00001")
                .build();

        mvc.perform(post("/add/deleteAddItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnAddInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void delteAddItem_GfnAddInfo_잘못된값() throws Exception {
        this.checkParam("/add/deleteAddItem", GfnAddInfo.builder().addItemCode(null).build(), this.NULL_MSG);
    }


}