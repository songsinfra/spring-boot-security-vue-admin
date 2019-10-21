package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.StatusCode;
import com.nexgrid.cgsg.admin.mapper.GfnEntrMapper;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnEntrInfo;
import com.nexgrid.cgsg.admin.vo.GfnMapInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional
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

        gfnEntrMapper.disableMapItemForEntr(GfnMapInfo.builder()
                .statusCd(gfnEntrInfo.getStatusCd())
                .entrItemCode(gfnEntrInfo.getEntrItemCode())
                .build());

        return gfnEntrMapper.updateEntrItem(gfnEntrInfo);
    }

    public int deleteEntrItem(String entrItemCode, String updateId) {
        Assert.hasLength(entrItemCode, "entrItemCode is null");

        return gfnEntrMapper.deleteEntrItem(entrItemCode, updateId);
    }

    public List<GfnAddInfo> selectEntrMappingList(String entrItemCode) {
        Assert.hasLength(entrItemCode, "entrItemCode is null");

        return gfnEntrMapper.selectEntrMappingList(entrItemCode);
    }

    public int insertMapItemList(String entrItemCode, List<String> addItemCodeList) {
        Assert.hasLength(entrItemCode, "entrItemCode is null");
        Assert.notNull(addItemCodeList, "addItemCodeList is null");

        int insertCnt = 0;
        String statusCd = StatusCode.USED.getCode();
        String createId = "";

        //entrItemCode에 해당해는 addItemCode 전체 delete
        gfnEntrMapper.deleteMapItem(entrItemCode);

        for (String addItemCode: addItemCodeList) {
            insertCnt += gfnEntrMapper.insertMapItem(entrItemCode, addItemCode, statusCd, createId);
        }

        return insertCnt;
    }

    public int deleteMapItem(String entrItemCode) {
        Assert.hasLength(entrItemCode, "entrItemCode is null");

        return gfnEntrMapper.deleteMapItem(entrItemCode);
    }
}
