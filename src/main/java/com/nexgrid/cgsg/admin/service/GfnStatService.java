package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.GfnStatMapper;
import com.nexgrid.cgsg.admin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class GfnStatService {
    @Autowired
    private GfnStatMapper gfnStatMapper;

    public List<GfnMasterInfo> selectUserStat(GfnMasterInfoParam gfnMasterInfo) {
        Assert.notNull(gfnMasterInfo, "gfnMasterInfo is null");
        return gfnStatMapper.selectUserStat(gfnMasterInfo);
    }

    public List<GfnJoinStatMonthInfo> selectJoinUserStat(GfnJoinStatInfoParam joinStatInfo) {
        Assert.notNull(joinStatInfo, "joinStatInfo is null");
        Assert.hasLength(joinStatInfo.getDate(), "date is null");

        // 입력 날짜를 기준으로 통계날짜 세팅
        joinStatInfo.initDate();

        List<GfnJoinStatInfo> gfnJoinStatInfos = gfnStatMapper.selectJoinUserStat(joinStatInfo);

        return this.analyzeRate(gfnJoinStatInfos);
    }

    public List<GfnJoinStatMonthInfo> analyzeRate(List<GfnJoinStatInfo> gfnJoinStatInfos) {
        List<GfnJoinStatMonthInfo> list = new ArrayList<>();

        list.add(create(o -> o.getTotalSum(), "가입자", gfnJoinStatInfos));
        list.add(create(o -> o.getFreeSum(), "무료가입자", gfnJoinStatInfos));
        list.add(create(o -> o.getPaidSum(), "유료가입자", gfnJoinStatInfos));

        return list;
    }

    private GfnJoinStatMonthInfo create(Function<GfnJoinStatInfo, Integer> f, String joinType, List<GfnJoinStatInfo> infos) {
        GfnJoinStatMonthInfo build = GfnJoinStatMonthInfo.builder()
                .joinType(joinType)
                .lastYearData(f.apply(infos.get(0)))
                .prevMonthData(f.apply(infos.get(1)))
                .lastMonthData(f.apply(infos.get(2)))
                .currentMonthData(f.apply(infos.get(3)))
                .build();
        build.doChangeRate();
        return build;
    }



}
