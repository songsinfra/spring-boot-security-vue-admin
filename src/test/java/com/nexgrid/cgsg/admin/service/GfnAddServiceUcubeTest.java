package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.constants.SvcTermType;
import com.nexgrid.cgsg.admin.utils.TestObjectFactory;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GfnAddServiceUcubeTest extends BaseServiceTest {

    private final String ADD_ITEM_CODE = "TES00001";

    @Autowired
    private TestObjectFactory testObjectFactory;
    @Autowired
    private GfnAddService gfnAddService;

    @Before
    public void setUp() {
        this.gfnAddService.insertAddItemForUcube(testObjectFactory.getInitGfnAddInfoForUcube().addItemCode(ADD_ITEM_CODE)
                                                                  .build());
    }

    @Test
    @Transactional
    public void insertAddItemForUcube() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube()
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

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube()
                .addItemCode(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_addItemType_null() {
        this.assertException(IllegalArgumentException.class, "addItemType is null");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube()
                .addItemType(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_addItemType_잘못된값() {
        this.assertException(IllegalArgumentException.class, "addItemType is invalid");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube()
                .addItemType(SvcTermType.AVAILABLE_DATE.getType())
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_AddItemCode_null() {
        this.assertException(IllegalArgumentException.class, "addItemCode is null");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube()
                .addItemCode(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForUcube(gfnAddInfo);
    }


    @Test
    @Transactional
    public void updateAddItem_ucube() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForUcube()
                .addItemCode(ADD_ITEM_CODE)
                .build();

        int updateCnt = gfnAddService.updateAddItemForUcube(gfnAddInfo);
        assertThat(updateCnt).isEqualTo(1);
    }
}
