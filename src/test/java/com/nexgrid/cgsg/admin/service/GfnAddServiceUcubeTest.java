package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
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
public class GfnAddServiceUcubeTest extends BaseServiceTest {

    @Autowired
    private GfnAddService gfnAddService;

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
        this.assertException(IllegalArgumentException.class, "gfnAddInfo is null");

        GfnAddInfo gfnAddInfo = null;

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_addItemCode_null() {
        this.assertException(IllegalArgumentException.class, "addItemCode is null");

        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemCode(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_addItemType_null() {
        this.assertException(IllegalArgumentException.class, "addItemType is null");

        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemType(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_addItemType_잘못된값() {
        this.assertException(IllegalArgumentException.class, "addItemType is invalid");

        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemType(SvcTermType.AVAILABLE_DATE.getType())
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_AddItemCode_null() {
        this.assertException(IllegalArgumentException.class, "addItemCode is null");

        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemCode(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }


    @Test
    @Transactional
    public void updateAddItem_ucube() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForUcube()
                .addItemCode("TES00001")
                .build();

        int updateCnt = gfnAddService.updateAddItemForUcube(gfnAddInfo);
        assertThat(updateCnt).isEqualTo(1);
    }
}