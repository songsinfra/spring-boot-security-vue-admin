package com.nexgrid.cgsg.admin.mapper;

import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GfnAddMapper {

    List<GfnAddInfo> selectAddItemList(String addItemNm);

    GfnAddInfo selectAddItem(String addItemCode);

    int selectAddItemCount();

    int insertAddItem(GfnAddInfo gfnAddInfo);

    int updateAddItem(GfnAddInfo gfnAddInfo);

    int deleteAddItem(String addItemCode);

    List<String> getNvidiaPlanTypeList();
}
