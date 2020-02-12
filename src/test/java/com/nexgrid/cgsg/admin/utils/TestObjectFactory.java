package com.nexgrid.cgsg.admin.utils;

import com.nexgrid.cgsg.admin.constants.*;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnEntrInfo;
import com.nexgrid.cgsg.admin.vo.GfnJoinStatInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.springframework.stereotype.Component;

@Component
public class TestObjectFactory {
    public GfnAddInfo.GfnAddInfoBuilder getInitGfnAddInfoForGfn() {
        return GfnAddInfo.builder()
                         .addItemType(AddItemType.GFN.getType())
                         .addItemNm("테스트 부가서비스1")
                         .svcSellPrice(1000)
                         .svcBasePrice(5000)
                         .addItemDetail("디테일1")
                         .addItemNotice("notice1")
                         .svcTermType(SvcTermType.AVAILABLE_DATE.getType())
                         .svcTermUnit(SvcTermUnit.Month.getCode())
                         .statusCd(StatusCode.USED.getCode())
                         .nvidiaPlan("PREMIUM")
                         .statusCd(StatusCode.USED.getCode())
                         .svcTermNum(1);
    }

    public GfnAddInfo.GfnAddInfoBuilder getInitGfnAddInfoForUcube() {
        return GfnAddInfo.builder()
                         .addItemCode("UCUBE00001")
                         .addItemType(AddItemType.UCUBE.getType())
                         .addItemNm("테스트 부가서비스1")
                         .svcSellPrice(1000)
                         .svcBasePrice(5000)
                         .addItemDetail("디테일1")
                         .addItemNotice("notice1")
                         .svcTermType(SvcTermType.NONE.getType())
                         .nvidiaPlan("PREMIUM")
                         .statusCd(StatusCode.USED.getCode())
                ;
    }
    public GfnEntrInfo.GfnEntrInfoBuilder getGfnEntrInfo() {
        return GfnEntrInfo.builder()
                          .entrItemCode("ITEM999")
                          .socTypeCode(SocTypeCode.PROD.getCode())
                          .prodCd(ProdCode.MOBILE.getCode())
                          .entrItemNm("상품명")
                          .statusCd(StatusCode.USED.getCode())
                          .memo("메모");
    }

    public MbrInfo.MbrInfoBuilder getMbrInfo() {
        return MbrInfo.builder()
                                 .mbrId("Testid")
                                 .roleCd("R-001")
                                 .mbrPw("password")
                                 .mbrNm("관리자")
                                 .tel("01012341234")
                                 .email("test@test.co.kr")
                                 .mbrCompany("넥스그리드99")
                                 .mbrDptmt("컨버젼스팀")
                                 ;
    }

    public GfnJoinStatInfo.GfnJoinStatInfoBuilder getJoinStatInfo() {
        return GfnJoinStatInfo.builder()
                              .signupDate("20200212")
                              .freeUserCnt("10")
                              .paidUserCnt("20")
                              .totUserCnt("30")
                ;
    }
}
