package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.base.BaseControllerTest;
import com.nexgrid.cgsg.admin.constants.SvcTermType;
import com.nexgrid.cgsg.admin.service.GfnAddService;
import com.nexgrid.cgsg.admin.utils.TestObjectFactory;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnAddInfoParam;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TestObjectFactory testObjectFactory;
    @Autowired
    private GfnAddService gfnAddService;
    private final String ADD_ITEM_CODE = "TES00001";

    @Before
    public void setUp() {
        this.gfnAddService.insertAddItemForUcube(testObjectFactory.getInitGfnAddInfoForUcube().addItemCode(ADD_ITEM_CODE)
                                                                   .build());
    }

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
        String addItemCode = this.ADD_ITEM_CODE;
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
    @Test
    public void insertAddItem_gfn_AVAILABLE_DATE() throws Exception {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn().build();

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
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
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
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube().build();

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
        this.checkParam("/add/insertAddItem", testObjectFactory.getInitGfnAddInfoForGfn().addItemType("X").build(), "AddItemType is invalid");

        this.checkParam("/add/insertAddItem", testObjectFactory.getInitGfnAddInfoForGfn().addItemCode(genStr(11)).build(), this.OVER_SIZE_MSG);
        this.checkParam("/add/insertAddItem", testObjectFactory.getInitGfnAddInfoForGfn().addItemType(null).build(), this.NULL_MSG);
        this.checkParam("/add/insertAddItem", testObjectFactory.getInitGfnAddInfoForGfn().nvidiaPlan(null).build(), this.NULL_MSG);
        this.checkParam("/add/insertAddItem", testObjectFactory.getInitGfnAddInfoForGfn().addItemType(genStr(2)).build(), OVER_SIZE_MSG);

        this.checkParam("/add/insertAddItem", testObjectFactory.getInitGfnAddInfoForGfn().addItemNm(genStr(121)).build(), OVER_SIZE_MSG);
    }

    @Test
    public void updateAddItem_gfn_AVAILABLE_DATE() throws Exception {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemCode(ADD_ITEM_CODE)
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
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemCode(ADD_ITEM_CODE)
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
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube()
                .addItemCode(ADD_ITEM_CODE)
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
        this.checkParam("/add/updateAddItem", testObjectFactory.getInitGfnAddInfoForUcube().addItemType("X").build(), "AddItemType is invalid");
        this.checkParam("/add/updateAddItem", testObjectFactory.getInitGfnAddInfoForUcube().addItemCode(null).build(), this.NULL_MSG);
    }

    @Test
    public void deleteAddItem() throws Exception {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube()
                                    .addItemCode(ADD_ITEM_CODE)
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
