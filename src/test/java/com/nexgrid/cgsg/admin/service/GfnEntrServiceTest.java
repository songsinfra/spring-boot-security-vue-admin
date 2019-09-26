package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.constants.ProdCode;
import com.nexgrid.cgsg.admin.constants.SocTypeCode;
import com.nexgrid.cgsg.admin.constants.StatusCode;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnEntrInfo;
import com.nexgrid.cgsg.admin.vo.GfnMapInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnEntrServiceTest extends BaseServiceTest {

    @Autowired
    private GfnEntrService gfnEntrService;

    @Test
    public void selectEntrItem() {
        String entrItemCode = "ITEM0001";
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
    public void insertEntrItem() {
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo().build();
        int insertCnt = gfnEntrService.insertEntrItem(gfnEntrInfo);
        assertThat(insertCnt).isEqualTo(1);
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_null() {
        this.insertEntrItem_check_param(null, "gfnEntrInfo is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_entrItemCode() {
        this.insertEntrItem_check_param(this.getGfnEntrInfo().entrItemCode(null).build(), "EntrItemCode is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_SocTypeCode() {
        this.insertEntrItem_check_param(this.getGfnEntrInfo().socTypeCode(null).build(), "SocTypeCode is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_ProdCd() {
        this.insertEntrItem_check_param(this.getGfnEntrInfo().prodCd(null).build(), "ProdCd is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_EntrItemNm() {
        this.insertEntrItem_check_param(this.getGfnEntrInfo().entrItemNm(null).build(), "EntrItemNm is null");
    }

    @Test
    public void insertEntrItem_GfnEntrInfo_변수체크_StatusCd() {
        this.insertEntrItem_check_param(this.getGfnEntrInfo().statusCd(null).build(), "StatusCd is null");
    }

    public void insertEntrItem_check_param(GfnEntrInfo gfnEntrInfo, String exceptionMsg) {
        assertException(IllegalArgumentException.class, exceptionMsg);
        int insertCnt = gfnEntrService.insertEntrItem(gfnEntrInfo);
    }

    @Test
    public void updateEntrItem() {
        GfnEntrInfo gfnEntrInfo = this.getGfnEntrInfo()
                .entrItemCode("ITEM0001")
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

        GfnEntrInfo gfnEntrInfo = getGfnEntrInfo().entrItemCode(null).build();

        int updateCnt = gfnEntrService.updateEntrItem(gfnEntrInfo);
    }

    @Test
    public void deleteEntrItem() {
        String entrItemCode = "ITEM0001";
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
    public void selectAddItemListWithMap() {
        String entrItemCode = "ITEM0001";
        List<GfnAddInfo> gfnAddInfos = gfnEntrService.selectAddItemListWithMap(entrItemCode);
        assertThat(gfnAddInfos).size().isGreaterThan(0);
    }

    @Test
    public void selectAddItemListWithMap_entrItemCode_null() {
        assertException(IllegalArgumentException.class, "entrItemCode is null");

        String entrItemCode = null;
        List<GfnAddInfo> gfnAddInfos = gfnEntrService.selectAddItemListWithMap(entrItemCode);
    }

    @Test
    public void insertMapItem() {
        GfnMapInfo build = GfnMapInfo.builder()
                .addItemCode("TES00999")
                .entrItemCode("ITEM0001")
                .statusCd(StatusCode.USE.getCode())
                .build();

        int insertCnt = gfnEntrService.insertMapItem(build);

        assertThat(insertCnt).isEqualTo(1);
    }

    @Test
    public void insertMapItem_gfnMapInfo_null() {
        assertException(IllegalArgumentException.class, "gfnMapInfo is null");

        GfnMapInfo build = null;

        int insertCnt = gfnEntrService.insertMapItem(build);
    }

    @Test
    public void insertMapItem_gfnMapInfo_AddItemCode_null() {
        assertException(IllegalArgumentException.class, "AddItemCode is null");

        GfnMapInfo build = GfnMapInfo.builder()
                .addItemCode(null)
                .entrItemCode("ITEM0001")
                .statusCd(StatusCode.USE.getCode())
                .build();

        gfnEntrService.insertMapItem(build);
    }

    @Test
    public void insertMapItem_gfnMapInfo_EntrItemCode_null() {
        assertException(IllegalArgumentException.class, "EntrItemCode is null");

        GfnMapInfo build = GfnMapInfo.builder()
                .addItemCode("TES00999")
                .entrItemCode(null)
                .statusCd(StatusCode.USE.getCode())
                .build();

        gfnEntrService.insertMapItem(build);
    }

    @Test
    public void deleteMapItem() {
        String entrItemCode = "ITEM0001";
        String addItemCode = "TES00001";
        int deleteCnt = gfnEntrService.deleteMapItem(entrItemCode, addItemCode);

        assertThat(deleteCnt).isEqualTo(1);
    }

    @Test
    public void deleteMapItem_entrItemCode_null() {
        assertException(IllegalArgumentException.class, "entrItemCode is null");

        String entrItemCode = null;
        String addItemCode = "TES00001";

        gfnEntrService.deleteMapItem(entrItemCode, addItemCode);
    }

    @Test
    public void deleteMapItem_addItemCode_null() {
        assertException(IllegalArgumentException.class, "addItemCode is null");

        String entrItemCode = "ITEM0001";
        String addItemCode = null;
        gfnEntrService.deleteMapItem(entrItemCode, addItemCode);
    }


}