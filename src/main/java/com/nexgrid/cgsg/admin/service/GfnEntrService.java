package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.GfnEntrMapper;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnEntrInfo;
import com.nexgrid.cgsg.admin.vo.GfnMapInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class GfnEntrService {

    @Autowired
    private GfnEntrMapper gfnEntrMapper;


    public GfnEntrInfo selectEntrItem(String entrItemCode) {
        Assert.hasLength(entrItemCode, "entrItemCode is null");

        return gfnEntrMapper.selectEntrItem(entrItemCode);
    }

    public List<GfnEntrInfo> selectEntrItemList(String entrItemNm) {
        return gfnEntrMapper.selectEntrItemList(entrItemNm);
    }

    public int insertEntrItem(GfnEntrInfo gfnEntrInfo) {
        Assert.notNull(gfnEntrInfo, "gfnEntrInfo is null");
        Assert.hasLength(gfnEntrInfo.getEntrItemCode(), "EntrItemCode is null");
        Assert.hasLength(gfnEntrInfo.getSocTypeCode(), "SocTypeCode is null");
        Assert.hasLength(gfnEntrInfo.getProdCd(), "ProdCd is null");
        Assert.hasLength(gfnEntrInfo.getEntrItemNm(), "EntrItemNm is null");
        Assert.hasLength(gfnEntrInfo.getStatusCd(), "StatusCd is null");

        return gfnEntrMapper.insertEntrItem(gfnEntrInfo);
    }

    public int updateEntrItem(GfnEntrInfo gfnEntrInfo) {
        Assert.notNull(gfnEntrInfo, "gfnEntrInfo is null");
        Assert.hasLength(gfnEntrInfo.getEntrItemCode(), "EntrItemCode is null");

        return gfnEntrMapper.updateEntrItem(gfnEntrInfo);
    }

    public int deleteEntrItem(String entrItemCode, String updateId) {
        Assert.hasLength(entrItemCode, "entrItemCode is null");

        return gfnEntrMapper.deleteEntrItem(entrItemCode, updateId);
    }

    public List<GfnAddInfo> selectAddItemListWithMap(String entrItemCode) {
        Assert.hasLength(entrItemCode, "entrItemCode is null");

        return gfnEntrMapper.selectAddItemListWithMap(entrItemCode);
    }

    public int insertMapItem(GfnMapInfo gfnMapInfo) {
        Assert.notNull(gfnMapInfo, "gfnMapInfo is null");
        Assert.hasLength(gfnMapInfo.getAddItemCode(), "AddItemCode is null");
        Assert.hasLength(gfnMapInfo.getEntrItemCode(), "EntrItemCode is null");

        return gfnEntrMapper.insertMapItem(gfnMapInfo);
    }

    public int deleteMapItem(String entrItemCode, String addItemCode) {
        Assert.hasLength(entrItemCode, "entrItemCode is null");
        Assert.hasLength(addItemCode, "addItemCode is null");

        return gfnEntrMapper.deleteMapItem(entrItemCode, addItemCode);
    }
}
