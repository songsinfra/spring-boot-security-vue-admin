package com.nexgrid.cgsg.admin.mapper;

import com.nexgrid.cgsg.admin.vo.GfnPromoInfo;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfoParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GfnPromoMapper {
    int insertPromo(GfnPromoInfo promoInfo);
    List<GfnPromoInfo> selectPromoList(GfnPromoInfoParam promoInfo);
    List<GfnPromoInfo> existPromoUserInfo(GfnPromoInfoParam promoInfo);
    int updatePromo(GfnPromoInfoParam promoInfo);
    int updateGfnMaster(String gfnId, String dueDt);
    int deletePromo(String promoCode);
}
