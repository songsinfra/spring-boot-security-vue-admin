package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.GfnStatMapper;
import com.nexgrid.cgsg.admin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class GfnStatService {
    @Autowired
    private GfnStatMapper gfnStatMapper;

    public List<GfnMasterInfo> selectUserDetailStat(GfnMasterInfoParam gfnMasterInfo) {
        Assert.notNull(gfnMasterInfo, "gfnMasterInfo is null");
        return gfnStatMapper.selectUserDetailStat(gfnMasterInfo);
    }

    public List<GfnJoinStatMonthInfo> selectJoinUserStat(GfnJoinStatInfoParam joinStatInfo) {
        Assert.notNull(joinStatInfo, "joinStatInfo is null");
        Assert.hasLength(joinStatInfo.getDate(), "date is null");

        // 입력 날짜를 기준으로 통계날짜 세팅
        joinStatInfo.initDate();

        List<GfnJoinStatMonthInfo> gfnJoinStatInfos = gfnStatMapper.selectJoinUserStat(joinStatInfo);
        gfnJoinStatInfos.stream().forEach(i->i.doChangeRate());

        return gfnJoinStatInfos;
    }

    public List<GfnJoinStatDetailInfo> selectJoinStatDetailList(GfnJoinStatDetailInfoParam joinStatDetailInfo) {
        Assert.notNull(joinStatDetailInfo, "joinStatDetailInfo is null");
        Assert.hasLength(joinStatDetailInfo.getStartDt(), "startDt is null");
        Assert.hasLength(joinStatDetailInfo.getEndDt(), "endDt is null");
        
        return gfnStatMapper.selectJoinStatDetailList(joinStatDetailInfo);
    }
}
