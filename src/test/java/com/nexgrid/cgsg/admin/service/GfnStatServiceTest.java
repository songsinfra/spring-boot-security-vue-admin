package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.utils.TestObjectFactory;
import com.nexgrid.cgsg.admin.vo.*;
import org.junit.Before;
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
public class GfnStatServiceTest extends BaseServiceTest {

    @Autowired
    private GfnStatService gfnStatService;

    @Autowired
    private TestObjectFactory testObjectFactory;

    @Before
    public void setup() {
        gfnStatService.insertJoiStat(testObjectFactory.getJoinStatInfo().signupDate("20200211").build());
        gfnStatService.insertJoiStat(testObjectFactory.getJoinStatInfo().signupDate("20200212").build());
        gfnStatService.insertJoiStat(testObjectFactory.getJoinStatInfo().signupDate("20200213").build());
        gfnStatService.insertJoiStat(testObjectFactory.getJoinStatInfo().signupDate("20200214").build());
    }

    @Test
    public void selectUserStat() {
        GfnMasterInfoParam gfnMasterInfo = GfnMasterInfoParam.builder()
                .entrItemCode("LPZ0000999")
//                .addItemCode("GFN0000007")
//                .createStartDt("20191101")
//                .createEndDt("20191101")
//                .subNo("500137677005")
//                .ctn("010043307609")
                .build();

        List<GfnMasterInfo> gfnMasterInfos = gfnStatService.selectUserDetailStat(gfnMasterInfo);
        assertThat(gfnMasterInfos).size().isGreaterThan(0);
    }

    @Test
    public void selectUserStat_빈값호출() {
        GfnMasterInfoParam gfnMasterInfo = GfnMasterInfoParam.builder()
                .build();

        List<GfnMasterInfo> gfnMasterInfos = gfnStatService.selectUserDetailStat(gfnMasterInfo);
        assertThat(gfnMasterInfos).size().isGreaterThan(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectUserStat_null() {
        GfnMasterInfoParam gfnMasterInfo = null;
        gfnStatService.selectUserDetailStat(gfnMasterInfo);
    }

    @Test
    public void selectJoinUserStat() {
        GfnJoinStatInfoParam joinStatInfo = new GfnJoinStatInfoParam();
        joinStatInfo.setDate("20200211");

        List<GfnJoinStatMonthInfo> gfnJoinStatMonthInfos = gfnStatService.selectJoinUserStat(joinStatInfo);
        assertThat(gfnJoinStatMonthInfos).size().isEqualTo(3);
        assertThat(gfnJoinStatMonthInfos.get(0).getJoinType()).isEqualTo("가입자");
        assertThat(gfnJoinStatMonthInfos.get(1).getJoinType()).isEqualTo("무료가입자");
        assertThat(gfnJoinStatMonthInfos.get(2).getJoinType()).isEqualTo("유료가입자");
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectJoinUserStat_null() {
        GfnJoinStatInfoParam joinStatInfo = null;
        gfnStatService.selectJoinUserStat(joinStatInfo);
    }


    @Test
    public void selectJoinStatDetailList() {
        GfnJoinStatDetailInfoParam joinStatDetailInfo = GfnJoinStatDetailInfoParam.builder()
                .startDt("20181001")
                .endDt("20201030")
                .build();
        List<GfnJoinStatDetailInfo> gfnJoinStatDetailInfos = gfnStatService.selectJoinStatDetailList(joinStatDetailInfo);

        assertThat(gfnJoinStatDetailInfos).size().isGreaterThan(0);
    }

    @Test()
    public void selectJoinStatDetailList_startDt_null() {
        assertException(IllegalArgumentException.class, "startDt is null");

        GfnJoinStatDetailInfoParam joinStatDetailInfo = GfnJoinStatDetailInfoParam.builder()
                .startDt("")
                .endDt("20191030")
                .build();

        gfnStatService.selectJoinStatDetailList(joinStatDetailInfo);
    }

    @Test
    public void selectGfnTosList() {
        List<GfnTosInfo> list = gfnStatService.selectGfnTosList();
        System.out.println("list = " + list);

        assertThat(list).size()
                        .isGreaterThan(0);
    }

    @Test
    public void selectAccepTosList() {
        String gfnid = "91c3183a898d62943a72e2ea3f802d5a131548241ef10c48a43ed667d8e70a9d";

        List<AcceptTosInfo> list = gfnStatService.selectAccepTosList(gfnid);
        System.out.println("list = " + list);

        assertThat(list).size()
                        .isGreaterThan(0);

        assertThat(list.get(0)
                       .getTosNo()).isNotEmpty();
    }

    @Test
    public void selectGfnTosListWithAcceptTos() {
        String gfnId = "91c3183a898d62943a72e2ea3f802d5a131548241ef10c48a43ed667d8e70a9d";

        List<GfnTosInfo> gfnTosInfos = gfnStatService.selectGfnTosListWithAcceptTos(gfnId);
        System.out.println("gfnTosInfos = " + gfnTosInfos);

        assertThat(gfnTosInfos.size()).isGreaterThan(0);
        assertThat(gfnTosInfos.get(1)
                              .getIsAgree()).isNotEmpty();
    }

    @Test
    public void updateAcceptTosList() {
        String agreeList = "";
        String gfnId = "";
        gfnStatService.updateAcceptTosList(gfnId, agreeList);
    }
}
