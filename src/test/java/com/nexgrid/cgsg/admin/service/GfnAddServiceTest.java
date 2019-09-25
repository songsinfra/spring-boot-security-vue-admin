package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.AddItemType;
import com.nexgrid.cgsg.admin.constants.SvcTermType;
import com.nexgrid.cgsg.admin.constants.SvcTermUnit;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnAddServiceTest {

    @Autowired
    private GfnAddService gfnAddService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

//    @Test
//    public void 테스트용데이터생성() {
//        GfnAddInfo gfnAddInfo = GfnAddInfo.builder()
//                .addItemCode("TES00001")
//                .addItemType(AddItemType.GFN.getType())
//                .addItemNm("테스트 부과서비스")
//                .svcSellPrice(1000)
//                .svcBasePrice(500)
//                .addItemDetail("디테일")
//                .addItemNotice("notice")
//                .svcTermType(SvcTermType.NONE.getType())
//                .build();
//
//
//        int insertCnt = gfnAddService.insertAddItem(gfnAddInfo);
//
//        assertThat(insertCnt).isGreaterThan(0);
//    }

    @Test
    @Transactional
    public void selectAddItem() {
        String addItemCode = "TES00001";
        GfnAddInfo gfnAddInfo = gfnAddService.selectAddItem(addItemCode);

        assertThat(gfnAddInfo).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void selectAddItem_addItemCode_null() {
        String addItemCode = null;
        gfnAddService.selectAddItem(addItemCode);
    }

    @Test
    public void selectAddItemList() {
        String addItemNm = "";

        List<GfnAddInfo> gfnAddInfos = gfnAddService.selectAddItemList(addItemNm);

        assertThat(gfnAddInfos).size().isGreaterThan(0);
    }

    @Test
    public void selectAddItemList_itemNm_검색() {
        String addItemNm = "테스트";

        List<GfnAddInfo> gfnAddInfos = gfnAddService.selectAddItemList(addItemNm);

        assertThat(gfnAddInfos).size().isGreaterThan(0);
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록() {
        GfnAddInfo gfnAddInfo = GfnAddInfo.builder()
                .addItemType(AddItemType.GFN.getType())
                .addItemNm("테스트 부과서비스1")
                .svcSellPrice(1000)
                .addItemDetail("디테일1")
                .addItemNotice("notice1")
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermUnit(SvcTermUnit.Month.getCode())
                .svcTermNum(1)
                .build();

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);

        assertThat(insertCnt).isGreaterThan(0);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_null() {
        GfnAddInfo gfnAddInfo = null;

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);
    }

    private GfnAddInfo.GfnAddInfoBuilder getInitGfnAddInfoForGfn() {
        return GfnAddInfo.builder()
                .addItemType(AddItemType.GFN.getType())
                .addItemNm("테스트 부과서비스1")
                .svcSellPrice(1000)
                .addItemDetail("디테일1")
                .addItemNotice("notice1")
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermUnit(SvcTermUnit.Month.getCode())
                .svcTermNum(1);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_addItemType_null() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .addItemType(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_SvcTermType_잘못된값() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.NONE.getType())
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "invalid");

    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_SvcTermUnit_잘못된값() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermUnit("aaa")
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "SvcTermUnit value is only M,D,H Code");
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_SvcTermUnit_Null() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermNum(null)
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "SvcTermUnit is null");
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_GfnAddInfo_SvcTermType_로직처리() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.LIMIT_DATE.getType())
                .svcTermDate(new Date())
                .build();

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);

        assertThat(insertCnt).isGreaterThan(0);
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_GfnAddInfo_SvcTermType_로직처리_svcTermDate_값없음() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.LIMIT_DATE.getType())
                .svcTermDate(null)
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "SvcTermDate is null");
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_gfnAddInfo_값없음() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.LIMIT_DATE.getType())
                .svcTermDate(null)
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "SvcTermDate is null");
    }


    public void exception_insertAddItemForGfn(GfnAddInfo gfnAddInfo, Class exception, String exceptionMsg) {
        expectedException.expect(exception);
        expectedException.expectMessage(CoreMatchers.containsString(exceptionMsg));

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);
    }

    @Test
    public void generateAddItemCode() {
        String newAddItemCode = gfnAddService.generateAddItemCode();
        System.out.println("newAddItemCode = " + newAddItemCode);

        assertThat(newAddItemCode).hasSize(10);
    }

    @Test
    public void convertToFullTime() {
        Date date = new Date();

        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
                                    .plusDays(1).minus(1, ChronoUnit.SECONDS);
        Date compareDate = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("compareDate = " + compareDate);

        Date resultTimeDate = gfnAddService.convertToFullTime(date);
        System.out.println("resultTimeDate = " + resultTimeDate);

        assertThat(compareDate).isEqualTo(resultTimeDate);
    }

    @Test
    @Transactional
    public void updateAddItem() {
        GfnAddInfo gfnAddInfo = this.getInitGfnAddInfoForGfn()
                .addItemCode("TES00001")
                .build();

        int updateCnt = gfnAddService.updateAddItem(gfnAddInfo);
        assertThat(updateCnt).isEqualTo(1);
    }

    @Test
    public void deleteAddItem() {
        String addItemCode = "TES00001";
        int deleteCnt = gfnAddService.deleteAddItem(addItemCode);

        assertThat(deleteCnt).isEqualTo(1);
    }
}