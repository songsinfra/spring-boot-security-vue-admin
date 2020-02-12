package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.constants.AddItemType;
import com.nexgrid.cgsg.admin.constants.SvcTermType;
import com.nexgrid.cgsg.admin.constants.SvcTermUnit;
import com.nexgrid.cgsg.admin.utils.StringUtil;
import com.nexgrid.cgsg.admin.utils.TestObjectFactory;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnAddServiceTest extends BaseServiceTest {

    private String ADD_ITEM_CODE;
    @Autowired
    private TestObjectFactory testObjectFactory;

    @Autowired
    private GfnAddService gfnAddService;

    @Before
    public void setUp() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                                            .addItemCode(ADD_ITEM_CODE)
                                            .build();

        this.gfnAddService.insertAddItemForGfn(gfnAddInfo);

        this.ADD_ITEM_CODE = gfnAddInfo.getAddItemCode();
    }
//    @Test
//    public void 테스트용데이터생성() {
//        GfnAddInfo gfnAddInfo = GfnAddInfo.builder()
//                .addItemCode("TES00001")
//                .addItemType(AddItemType.GFN.getType())
//                .addItemNm("테스트 부가서비스")
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
        String addItemCode = this.ADD_ITEM_CODE;
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
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn().build();

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);

        assertThat(insertCnt).isGreaterThan(0);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_null() {
        GfnAddInfo gfnAddInfo = null;

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_statusCd_null() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemType(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_addItemType_null() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemType(null)
                .build();

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_SvcTermType_잘못된값() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.NONE.getType())
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "invalid");

    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_SvcTermUnit_잘못된값() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermUnit("aaa")
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "SvcTermUnit value is only M,D,H Code");
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_gfnAddInfo_svcTermNum_Null() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermUnit(SvcTermUnit.Day.getCode())
                .svcTermNum(null)
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "svcTermNum is null");
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_GfnAddInfo_SvcTermType_로직처리() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.LIMIT_DATE.getType())
                .svcTermDate(new Date())
                .build();

        int insertCnt = gfnAddService.insertAddItemForGfn(gfnAddInfo);

        assertThat(insertCnt).isGreaterThan(0);
    }

    @Test
    @Transactional
    public void insertAddItemForGfn_부가서비스_등록_GfnAddInfo_SvcTermType_로직처리_svcTermDate_값없음() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.LIMIT_DATE.getType())
                .svcTermDate(null)
                .build();

        this.exception_insertAddItemForGfn(gfnAddInfo, IllegalArgumentException.class, "SvcTermDate is null");
    }

    @Test
    @Transactional
    public void insertAddItemForUcube_gfnAddInfo_값없음() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
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
    public void updateAddItem_gfn() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemCode(this.ADD_ITEM_CODE)
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
        assertThat(updateCnt).isEqualTo(1);
    }

    @Test
    @Transactional
    public void updateAddItem_gfn_AVAILABLE_DATE_Day() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemCode(this.ADD_ITEM_CODE)
                .svcTermUnit(SvcTermUnit.Day.getCode())
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
        assertThat(updateCnt).isEqualTo(1);
    }

    @Test
    @Transactional
    public void updateAddItem_gfn_AVAILABLE_DATE_Hour() {
        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemCode(this.ADD_ITEM_CODE)
                .svcTermUnit(SvcTermUnit.Hour.getCode())
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
        assertThat(updateCnt).isEqualTo(1);
    }

    @Test
    @Transactional
    public void updateAddItem_gfn_addItemType_null() {
        assertException(IllegalArgumentException.class, "addItemType is null");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemType(null)
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
    }

    @Test
    @Transactional
    public void updateAddItem_gfn_addItemType_잘못된값() {
        assertException(IllegalArgumentException.class, "addItemType is invalid");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemType(AddItemType.UCUBE.getType())
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
    }

    @Test
    @Transactional
    public void updateAddItem_gfn_SvcTermType_잘못된값() {
        assertException(IllegalArgumentException.class, "SvcTermType is invalid");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.NONE.getType())
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
    }

    @Test
    @Transactional
    public void updateAddItem_gfn_SvcTermUnit_잘못된값() {
        assertException(IllegalArgumentException.class, "SvcTermUnit value is only M,D,H Code");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermUnit("XX")
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
    }

    @Test
    @Transactional
    public void updateAddItem_gfn_svcTermNum_null() {
        assertException(IllegalArgumentException.class, "svcTermNum is null");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                .svcTermUnit(SvcTermUnit.Day.getCode())
                .svcTermNum(null)
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
    }

    @Test
    @Transactional
    public void updateAddItem_gfn_addItemCode_null() {
        assertException(IllegalArgumentException.class, "addItemCode is null");

        GfnAddInfo gfnAddInfo = testObjectFactory.getInitGfnAddInfoForGfn()
                .addItemCode(null)
                .build();

        int updateCnt = gfnAddService.updateAddItemForGfn(gfnAddInfo);
    }

    @Test
    public void deleteAddItem() {
        int deleteCnt = gfnAddService.deleteAddItem(this.ADD_ITEM_CODE);

        assertThat(deleteCnt).isEqualTo(1);
    }

    @Test
    public void convertNewLineToDd() {
        String dd = "<dd>부가서비스명</dd><dd>다음줄</dd>";
        String newLine = "부가서비스명\n다음줄";

        String result = StringUtil.convertNewListToDd(newLine);

        assertThat(result).isEqualTo(dd);
    }

    @Test
    public void convertDdToNewLine() {
        String dd = "<dd>부가서비스명</dd><dd>다음줄</dd>";
        String newLine = "부가서비스명\n다음줄";

        String result = StringUtil.convertDdToNewLine(dd);
        assertThat(result).isEqualTo(newLine);
    }
}
