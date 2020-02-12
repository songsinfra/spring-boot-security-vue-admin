package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.utils.TestObjectFactory;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnEntrInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnEntrServiceTest extends BaseServiceTest {

    private final String ENTR_ITEM_CODE = "ITEM0001";
    @Autowired
    private GfnEntrService gfnEntrService;

    @Autowired
    private TestObjectFactory testObjectFactory;

    @Before
    public void setUp() {
       gfnEntrService.insertEntrItem(testObjectFactory.getGfnEntrInfo()
                                                       .entrItemCode(this.ENTR_ITEM_CODE)
                                                       .build());

       gfnEntrService.insertMapItemList("creatId", this.ENTR_ITEM_CODE, Arrays.asList("test1"));    }

    @Test
    public void selectEntrItem() {
        String entrItemCode = this.ENTR_ITEM_CODE;
        GfnEntrInfo gfnEntrInfo = gfnEntrService.selectEntrItem(entrItemCode);
        assertThat(gfnEntrInfo.getEntrItemCode()).isEqualTo(entrItemCode);
    }

    @Test
    public void selectEntrItem_entrItemCode_null() {
        assertException(IllegalArgumentException.class, "entrItemCode is null");

        String entrItemCode = null;
        GfnEntrInfo gfnEntrInfo = gfnEntrService.selectEntrItem(entrItemCode);
    }

    @Test
    public void selectEntrItemList() {
        String entrItemNm = "";
        List<GfnEntrInfo> gfnEntrInfos = gfnEntrService.selectEntrItemList(entrItemNm);

        assertThat(gfnEntrInfos).size().isGreaterThan(0);
    }

    @Test
    public void selectEntrItemList_entrItemNm_검색() {
        String entrItemNm = "상품";
        List<GfnEntrInfo> gfnEntrInfos = gfnEntrService.selectEntrItemList(entrItemNm);

        assertThat(gfnEntrInfos).size().isGreaterThan(0);
    }

    @Test
    public void insertEntrItem() {
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo().build();
        int insertCnt = gfnEntrService.insertEntrItem(gfnEntrInfo);
        assertThat(insertCnt).isEqualTo(1);
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_null() {
        this.insertEntrItem_check_param(null, "gfnEntrInfo is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_entrItemCode() {
        this.insertEntrItem_check_param(testObjectFactory.getGfnEntrInfo().entrItemCode(null).build(), "EntrItemCode is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_SocTypeCode() {
        this.insertEntrItem_check_param(testObjectFactory.getGfnEntrInfo().socTypeCode(null).build(), "SocTypeCode is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_ProdCd() {
        this.insertEntrItem_check_param(testObjectFactory.getGfnEntrInfo().prodCd(null).build(), "ProdCd is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_EntrItemNm() {
        this.insertEntrItem_check_param(testObjectFactory.getGfnEntrInfo().entrItemNm(null).build(), "EntrItemNm is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_StatusCd() {
        this.insertEntrItem_check_param(testObjectFactory.getGfnEntrInfo().statusCd(null).build(), "StatusCd is null");
    }

    public void insertEntrItem_check_param(GfnEntrInfo gfnEntrInfo, String exceptionMsg) {
        assertException(IllegalArgumentException.class, exceptionMsg);
        int insertCnt = gfnEntrService.insertEntrItem(gfnEntrInfo);
    }

    @Test
    public void updateEntrItem() {
        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo()
                .entrItemCode(ENTR_ITEM_CODE)
                .build();
        int updateCnt = gfnEntrService.updateEntrItem(gfnEntrInfo);
        assertThat(updateCnt).isEqualTo(1);
    }

    @Test
    public void updateEntrItem_gfnEntrInfo_null() {
        assertException(IllegalArgumentException.class, "gfnEntrInfo is null");

        GfnEntrInfo gfnEntrInfo = null;
        int updateCnt = gfnEntrService.updateEntrItem(gfnEntrInfo);
    }

    @Test
    public void updateEntrItem_gfnEntrInfo_EntrItemCode_null() {
        assertException(IllegalArgumentException.class, "EntrItemCode is null");

        GfnEntrInfo gfnEntrInfo = testObjectFactory.getGfnEntrInfo().entrItemCode(null).build();

        int updateCnt = gfnEntrService.updateEntrItem(gfnEntrInfo);
    }

    @Test
    public void deleteEntrItem() {
        String entrItemCode = this.ENTR_ITEM_CODE;
        String updateId = "";
        int deleteCnt = gfnEntrService.deleteEntrItem(entrItemCode, updateId);
        assertThat(deleteCnt).isEqualTo(1);
    }

    @Test
    public void deleteEntrItem_entrItemCode_null() {
        assertException(IllegalArgumentException.class, "entrItemCode is null");

        String entrItemCode = null;
        String updateId = "";
        int deleteCnt = gfnEntrService.deleteEntrItem(entrItemCode, updateId);
    }


    @Test
    public void selectEntrMappingList() {
        String entrItemCode = this.ENTR_ITEM_CODE;
        List<GfnAddInfo> gfnAddInfos = gfnEntrService.selectEntrMappingList(entrItemCode);
        assertThat(gfnAddInfos).size().isGreaterThan(0);
    }

    @Test
    public void selectEntrMappingList_entrItemCode_null() {
        assertException(IllegalArgumentException.class, "entrItemCode is null");

        String entrItemCode = null;
        List<GfnAddInfo> gfnAddInfos = gfnEntrService.selectEntrMappingList(entrItemCode);
    }

    @Test
    public void insertMapItemList() {
        String entrItemCode = "ITEM0002";
        List<String> addItemCodeList = Arrays.asList("test1");

        int insertCnt = gfnEntrService.insertMapItemList("creatId", entrItemCode, addItemCodeList);

        assertThat(insertCnt).isEqualTo(1);
    }

    @Test
    public void insertMapItemList_entrItemCode_null() {
        assertException(IllegalArgumentException.class, "entrItemCode is null");
        String entrItemCode = "";
        List<String> addItemCodeList = null;

        int insertCnt = gfnEntrService.insertMapItemList("creatId", entrItemCode, addItemCodeList);
    }

    @Test
    public void insertMapItemList_gfnMapInfo_AddItemCode_null() {
        assertException(IllegalArgumentException.class, "addItemCodeList is null");

        String entrItemCode = this.ENTR_ITEM_CODE;
        List<String> addItemCodeList = null;

        int insertCnt = gfnEntrService.insertMapItemList("creatId", entrItemCode, addItemCodeList);
    }

    @Test
    public void deleteMapItem() {
        String entrItemCode = this.ENTR_ITEM_CODE;
        int deleteCnt = gfnEntrService.deleteMapItem(entrItemCode);

        assertThat(deleteCnt).isGreaterThan(0);
    }

    @Test
    public void deleteMapItem_entrItemCode_null() {
        assertException(IllegalArgumentException.class, "entrItemCode is null");

        String entrItemCode = null;

        gfnEntrService.deleteMapItem(entrItemCode);
    }
}
