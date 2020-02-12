package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.base.BaseControllerTest;
import com.nexgrid.cgsg.admin.constants.StatusCode;
import com.nexgrid.cgsg.admin.service.GfnEntrService;
import com.nexgrid.cgsg.admin.utils.TestObjectFactory;
import com.nexgrid.cgsg.admin.vo.EntrMppingListParam;
import com.nexgrid.cgsg.admin.vo.GfnEntrInfo;
import com.nexgrid.cgsg.admin.vo.GfnMapInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
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
public class GfnEntrControllerTest extends BaseControllerTest {

    @Autowired
    private TestObjectFactory testObjectFactory;

    @Autowired
    private GfnEntrService gfnEntrService;
    private final String ENTR_ITEM_CODE = "ITEM0001";

    @Before
    public void setUp() {
        gfnEntrService.insertEntrItem(testObjectFactory.getGfnEntrInfo()
                                                       .entrItemCode(this.ENTR_ITEM_CODE)
                                                       .build());
    }

    @Test
    public void selectEntrItem() throws Exception {
        String entrItemCode = ENTR_ITEM_CODE;
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo()
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
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo()
                .entrItemCode(null)
                .build();

        this.checkParam("/entr/selectEntrItem", gfnEntrInfo, NULL_MSG);
    }


    @Test
    public void selectEntrItemList() throws Exception {
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo()
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
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo()
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
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo()
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

        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().entrItemCode(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().entrItemCode(genStr(11)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().socTypeCode(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().socTypeCode(genStr(2)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().prodCd(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().prodCd(genStr(21)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().entrItemNm(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().entrItemNm(genStr(121)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().statusCd(null).build(), NULL_MSG);
        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().statusCd(genStr(2)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertEntrItem", testObjectFactory.getGfnEntrInfo().memo(genStr(2049)).build(), OVER_SIZE_MSG);

    }

    @Test
    public void updateEntrItem() throws Exception {
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo()
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
    @WithUserDetails("admin")
    public void deleteEntrItem() throws Exception {
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo()
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
        this.checkParam("/entr/deleteEntrItem", testObjectFactory.getGfnEntrInfo().entrItemCode(null).build(), NULL_MSG);
    }

    private GfnMapInfo.GfnMapInfoBuilder getGfnMapInfo() {
        return GfnMapInfo.builder()
                .addItemCode("TES00999")
                .entrItemCode("ITEM0001")
                .statusCd(StatusCode.USED.getCode())
                ;
    }

    @Test
    public void selectEntrMappingList() throws Exception {
        GfnMapInfo gfnMapInfo = GfnMapInfo.builder().entrItemCode("ITEM0001").build();

        mvc.perform(post("/entr/selectEntrMappingList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gfnMapInfo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
        ;
    }

    @Test
    public void selectEntrMappingList_param_체크() throws Exception {
        this.checkParam("/entr/selectEntrMappingList", this.getGfnMapInfo().entrItemCode(null).build(), NULL_MSG);
    }

    @Test
    public void insertMapItemList() throws Exception {
        EntrMppingListParam mppingListParam = this.getEntrMappingParam().build();

        mvc.perform(post("/entr/insertMapItemList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mppingListParam))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    private EntrMppingListParam.EntrMppingListParamBuilder getEntrMappingParam() {
        return EntrMppingListParam.builder()
                .entrItemCode("ITEM0001")
                .addItemCodeList(Arrays.asList("test1", "test2"));
    }

    @Test
    public void insertMapItemList_param_체크() throws Exception {
        this.checkParam("/entr/insertMapItemList", this.getEntrMappingParam().entrItemCode(null).build(), NULL_MSG);
        this.checkParam("/entr/insertMapItemList", this.getEntrMappingParam().entrItemCode(genStr(11)).build(), OVER_SIZE_MSG);

        this.checkParam("/entr/insertMapItemList", this.getEntrMappingParam().addItemCodeList(null).build(), NULL_MSG);
    }
}

