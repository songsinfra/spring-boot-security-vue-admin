package com.nexgrid.cgsg.admin.mapper;

import com.nexgrid.cgsg.admin.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GfnStatMapper {
    List<GfnMasterInfo> selectUserDetailStat(GfnMasterInfoParam gfnMasterInfo);
    List<GfnJoinStatMonthInfo> selectJoinUserStat(GfnJoinStatInfoParam joinStatInfo);
    List<GfnJoinStatDetailInfo> selectJoinStatDetailList(GfnJoinStatDetailInfoParam joinStatDetailInfo);
    int insertJoiStat(GfnJoinStatInfo joinStatInfo);
    List<GfnTosInfo> selectGfnTosList();
    List<AcceptTosInfo> selectAccepTosList(String gfnId);

    int insertAcceptTos(AcceptTosInfo b01);

    int deleteOptionAcceptTos(String gfnId);
}
