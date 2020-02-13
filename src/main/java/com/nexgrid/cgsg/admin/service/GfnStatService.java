package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.GfnStatMapper;
import com.nexgrid.cgsg.admin.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    int insertJoiStat(GfnJoinStatInfo joinStatInfo) {
        return gfnStatMapper.insertJoiStat(joinStatInfo);
    }

    public List<GfnTosInfo> selectGfnTosListWithAcceptTos(String gfnId) {
        List<GfnTosInfo> gfnTosInfos = this.selectGfnTosList();
        List<AcceptTosInfo> acceptTosInfos = this.selectAccepTosList(gfnId);
        acceptTosInfos.forEach(atos->{
            Optional<GfnTosInfo> findTos = gfnTosInfos.stream()
                                                    .filter(tos -> tos.getTosNo()
                                                                      .equals(atos.getTosNo()))
                                                    .findFirst();
            if(findTos.isPresent()) findTos.get()
                                           .setIsAgree("1") ;

        });


        return gfnTosInfos;
    }

    public List<GfnTosInfo> selectGfnTosList() {
        return gfnStatMapper.selectGfnTosList();
    }

    public List<AcceptTosInfo> selectAccepTosList(String gfnId) {
        Assert.notNull(gfnId, "gfnId is null");
        Assert.hasLength(gfnId, "gfnId is empty");

        return gfnStatMapper.selectAccepTosList(gfnId);
    }

    public void updateAcceptTosList(String gfnId, String agreeList) {
        Assert.notNull(gfnId, "gfnId is null");

        gfnStatMapper.deleteOptionAcceptTos(gfnId);
        Stream.of(agreeList.split(","))
                .filter(i-> StringUtils.isNotEmpty(i))
                .forEach(tosNo-> gfnStatMapper.insertAcceptTos(AcceptTosInfo.builder()
                                                                           .gfnId(gfnId)
                                                                           .tosNo(tosNo)
                                                                           .acceptCd("B01")
                                                                           .build()));
    }
}
