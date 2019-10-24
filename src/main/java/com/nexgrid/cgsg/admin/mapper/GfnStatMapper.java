package com.nexgrid.cgsg.admin.mapper;

import com.nexgrid.cgsg.admin.vo.GfnJoinStatInfo;
import com.nexgrid.cgsg.admin.vo.GfnJoinStatInfoParam;
import com.nexgrid.cgsg.admin.vo.GfnMasterInfo;
import com.nexgrid.cgsg.admin.vo.GfnMasterInfoParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GfnStatMapper {
    List<GfnMasterInfo> selectUserStat(GfnMasterInfoParam gfnMasterInfo);
    List<GfnJoinStatInfo> selectJoinUserStat(GfnJoinStatInfoParam joinStatInfo);
}
