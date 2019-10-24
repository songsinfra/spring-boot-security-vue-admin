package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.vo.*;
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

    @Test
    public void selectUserStat() {
        GfnMasterInfoParam gfnMasterInfo = GfnMasterInfoParam.builder()
                .entrItemCode("2222222")
                .addItemCode("Test1234")
                .createStartDt("")
                .createEndDt("")
                .subNo("7")
                .ctn("ctn")
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
        joinStatInfo.setDate("20191023");

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
}