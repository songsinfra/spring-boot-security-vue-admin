package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.AddItemType;
import com.nexgrid.cgsg.admin.constants.SvcTermType;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GfnAddServiceUcubeTest {

    @Autowired
    private GfnAddService gfnAddService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private GfnAddInfo.GfnAddInfoBuilder getInitGfnAddInfoForUcube() {
        return GfnAddInfo.builder()
                .addItemCode("UCUBE00001")
                .addItemType(AddItemType.UCUBE.getType())
                .addItemNm("테스트 부과서비스1")
                .svcSellPrice(1000)
                .addItemDetail("디테일1")
                .addItemNotice("notice1")
                .svcTermType(SvcTermType.NONE.getType())
                ;
    }

    @Test
    @Transactional
    public void insertAddItemForUcube() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);

        assertThat(insertCnt).isEqualTo(1);
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_gfnAddInfo_null() {
        GfnAddInfo gfnAddInfo = null;

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "gfnAddInfo is null");
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_addItemType_null() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemType(null)
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "addItemType is null");
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_addItemType_잘못된값() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemType(SvcTermType.AVAILABLE_DATE.getType())
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "addItemType is invalid");
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_AddItemCode_null() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemCode(null)
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "addItemCode is null");
    }


    public void exception_insertAddItemForGfn(GfnAddInfo gfnAddInfo, Class exception, String exceptionMsg) {
        expectedException.expect(exception);
        expectedException.expectMessage(CoreMatchers.containsString(exceptionMsg));

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }




    @Test
    public void updateAddItem() {
    }

    @Test
    public void deleteAddItem() {
    }
}