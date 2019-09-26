package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.base.BaseControllerTest;
import com.nexgrid.cgsg.admin.constants.ProdCode;
import com.nexgrid.cgsg.admin.constants.SocTypeCode;
import com.nexgrid.cgsg.admin.constants.StatusCode;
import com.nexgrid.cgsg.admin.vo.GfnEntrInfo;
import com.nexgrid.cgsg.admin.vo.GfnMapInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnEntrControllerTest extends BaseControllerTest {

    private GfnEntrInfo.GfnEntrInfoBuilder getGfnEntrInfo() {
        return GfnEntrInfo.builder()
                .entrItemCode("ITEM999")
                .socTypeCode(SocTypeCode.PROD.getCode())
                .prodCd(ProdCode.MOBILE.getCode())
                .entrItemNm("상품명")
                .statusCd(StatusCode.USE.getCode())
                .memo("메모");
    }

    @Test
    public void selectEntrItem() throws Exception {
        String entrItemCode = "ITEM0001";
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo()
                .entrItemCode(entrItemCode)
                .build();

        mvc.perform(post("/entr/selectEntrItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnEntrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.entrItemCode").value(entrItemCode));
    }

    @Test
    public void selectEntrItem_entrItemCode_null() throws Exception {
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo()
                .entrItemCode(null)
                .build();

        this.checkParam("/entr/selectEntrItem", gfnEntrInfo, NULL_MSG);
    }


    @Test
    public void selectEntrItemList() throws Exception {
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo()
                .build();

        mvc.perform(post("/entr/selectEntrItemList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnEntrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void selectEntrItemList_entrItemNm_검색() throws Exception {
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo()
                .entrItemNm("상품명")
                .build();

        mvc.perform(post("/entr/selectEntrItemList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnEntrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void insertEntrItem() throws Exception {
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo()
                .build();

        mvc.perform(post("/entr/insertEntrItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnEntrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                ;
    }

    @Test
    public void insertEntrItem_param_체크() throws Exception {

        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().entrItemCode(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().entrItemCode(genStr(11)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().socTypeCode(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().socTypeCode(genStr(2)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().prodCd(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().prodCd(genStr(21)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().entrItemNm(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().entrItemNm(genStr(121)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().statusCd(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().statusCd(genStr(2)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", this.getGfnEntrInfo().memo(genStr(2049)).build(), OVER_SIZE_MSG);

    }

    @Test
    public void updateEntrItem() throws Exception {
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo()
                .entrItemCode("ITEM0001")
                .build();

        mvc.perform(post("/entr/updateEntrItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnEntrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithUserDetails("admin1")
    public void deleteEntrItem() throws Exception {
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo()
                .entrItemCode("ITEM0001")
                .build();

        mvc.perform(post("/entr/deleteEntrItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnEntrInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void deleteEntrItem_param_체크() throws Exception {
        this.checkParam("/entr/deleteEntrItem", this.getGfnEntrInfo().entrItemCode(null).build(), NULL_MSG);
    }

    private GfnMapInfo.GfnMapInfoBuilder getGfnMapInfo() {
        return GfnMapInfo.builder()
                .addItemCode("TES00999")
                .entrItemCode("ITEM0001")
                .statusCd(StatusCode.USE.getCode())
                ;
    }

    @Test
    public void selectAddItemListWithMap() throws Exception {
        GfnMapInfo gfnMapInfo = GfnMapInfo.builder().entrItemCode("ITEM0001").build();

        mvc.perform(post("/entr/selectAddItemListWithMap")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnMapInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
        ;
    }

    @Test
    public void selectAddItemListWithMap_param_체크() throws Exception {
        this.checkParam("/entr/selectAddItemListWithMap", this.getGfnMapInfo().entrItemCode(null).build(), NULL_MSG);
    }

    @Test
    public void insertMapItem() throws Exception {
        GfnMapInfo gfnMapInfo = this.getGfnMapInfo().build();

        mvc.perform(post("/entr/insertMapItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnMapInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void insertMapItem_param_체크() throws Exception {
        this.checkParam("/entr/insertMapItem", this.getGfnMapInfo().entrItemCode(null).build(), NULL_MSG);
        this.checkParam("/entr/insertMapItem", this.getGfnMapInfo().entrItemCode(genStr(11)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertMapItem", this.getGfnMapInfo().addItemCode(null).build(), NULL_MSG);
        this.checkParam("/entr/insertMapItem", this.getGfnMapInfo().addItemCode(genStr(11)).build(), OVER_SIZE_MSG);
    }

    @Test
    public void deleteMapItem() throws Exception {
        GfnMapInfo gfnMapInfo = GfnMapInfo.builder()
                .entrItemCode("ITEM0001")
                .addItemCode("TES00001")
                .build();

        mvc.perform(post("/entr/deleteMapItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnMapInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

}

