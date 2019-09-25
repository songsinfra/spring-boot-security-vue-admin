package com.nexgrid.cgsg.admin.mapper;

import com.nexgrid.cgsg.admin.vo.GfnEntrInfo;
import com.nexgrid.cgsg.admin.vo.GfnMapInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GfnEntrMapper {

    GfnEntrInfo selectEntrItem(String entrItemCode);

    List<GfnEntrInfo> selectEntrItemList(String entrItemNm);

    int insertEntrItem(GfnEntrInfo gfnEntrInfo);

    int updateEntrItem(GfnEntrInfo gfnEntrInfo);

    int deleteEntrItem(String entrItemCode, String updateId);

    List<GfnEntrInfo> selectAddItemListWithMap(String addItemCode);

    int insertMapItem(GfnMapInfo gfnMapInfo);

    int deleteMapItem(String addItemCode);
}
