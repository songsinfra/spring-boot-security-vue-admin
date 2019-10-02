package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.AddItemType;
import com.nexgrid.cgsg.admin.constants.SvcTermType;
import com.nexgrid.cgsg.admin.constants.SvcTermUnit;
import com.nexgrid.cgsg.admin.mapper.GfnAddMapper;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class GfnAddService {

    @Autowired
    private GfnAddMapper gfnAddMapper;

    public GfnAddInfo selectAddItem(String addItemCode) {
        Assert.hasLength(addItemCode, "addItemCode is null");

        return gfnAddMapper.selectAddItem(addItemCode);
    }

    public List<GfnAddInfo> selectAddItemList(String addItemNm) {
        return gfnAddMapper.selectAddItemList(addItemNm);
    }

    public int insertAddItemForGfn(GfnAddInfo gfnAddInfo) {
        this.validateDataForGfn(gfnAddInfo);

        // GFN ID 생성
        gfnAddInfo.setAddItemCode(this.generateAddItemCode());

        return this.setAddItemForGfn(info -> gfnAddMapper.insertAddItem((GfnAddInfo)info), gfnAddInfo);
    }

    public int updateAddItemForGfn(GfnAddInfo gfnAddInfo) {
        this.validateDataForGfn(gfnAddInfo);
        Assert.hasLength(gfnAddInfo.getAddItemCode(), "addItemCode is null");

        return this.setAddItemForGfn(info -> gfnAddMapper.updateAddItem((GfnAddInfo)info), gfnAddInfo);
    }

    private int setAddItemForGfn(Function function, GfnAddInfo gfnAddInfo) {
        if (StringUtils.equalsIgnoreCase(SvcTermType.LIMIT_DATE.getType(), gfnAddInfo.getSvcTermType())) {
            Date fullTime = this.convertToFullTime(gfnAddInfo.getSvcTermDate());
            gfnAddInfo.setSvcTermDate(fullTime);

            //clear unused Data svcTermData
            gfnAddInfo.setSvcTermUnit(null);
            gfnAddInfo.setSvcTermNum(null);
        }

        gfnAddInfo.setSvcBasePrice(0);

        return (int) function.apply(gfnAddInfo);
    }

    private void validateDataForGfn(GfnAddInfo gfnAddInfo) {
        Assert.notNull(gfnAddInfo, "gfnAddInfo is null");
        Assert.hasLength(gfnAddInfo.getAddItemType(),"addItemType is null");
        Assert.isTrue(StringUtils.equalsIgnoreCase(AddItemType.GFN.getType(), gfnAddInfo.getAddItemType()),
                "addItemType is invalid");
        Assert.isTrue(!StringUtils.equalsIgnoreCase(SvcTermType.NONE.getType(), gfnAddInfo.getSvcTermType()),
                "SvcTermType is invalid");

        if (StringUtils.equalsIgnoreCase(SvcTermType.AVAILABLE_DATE.getType(), gfnAddInfo.getSvcTermType())) {
            Assert.isTrue(StringUtils.equalsIgnoreCase(SvcTermUnit.Month.getCode(), gfnAddInfo.getSvcTermUnit()) ||
                            StringUtils.equalsIgnoreCase(SvcTermUnit.Month.getCode(), gfnAddInfo.getSvcTermUnit()) ||
                            StringUtils.equalsIgnoreCase(SvcTermUnit.Month.getCode(), gfnAddInfo.getSvcTermUnit()),
                    "SvcTermUnit value is only M,D,H Code");

            Assert.notNull(gfnAddInfo.getSvcTermNum(), "SvcTermUnit is null");
        }
    }

    public int insertAddItemForUcube(GfnAddInfo gfnAddInfo) {
        this.validateDataForUcude(gfnAddInfo);

        return gfnAddMapper.insertAddItem(gfnAddInfo);
    }

    public int updateAddItemForUcube(GfnAddInfo gfnAddInfo) {
        this.validateDataForUcude(gfnAddInfo);

        return gfnAddMapper.updateAddItem(gfnAddInfo);
    }

    private void validateDataForUcude(GfnAddInfo gfnAddInfo) {
        Assert.notNull(gfnAddInfo, "gfnAddInfo is null");
        Assert.hasLength(gfnAddInfo.getAddItemCode(),"addItemCode is null");
        Assert.hasLength(gfnAddInfo.getAddItemType(),"addItemType is null");
        Assert.isTrue(StringUtils.equalsIgnoreCase(AddItemType.UCUBE.getType(), gfnAddInfo.getAddItemType()),
                "addItemType is invalid");

        Assert.isTrue(StringUtils.equalsIgnoreCase(SvcTermType.NONE.getType(), gfnAddInfo.getSvcTermType()),
                "SvcTermType is invalid");

        Assert.hasLength(gfnAddInfo.getAddItemCode(), "addItemCode is null");
    }


    public String generateAddItemCode() {
        int count = gfnAddMapper.selectAddItemCount();
        return  "GFN" + StringUtils.leftPad(String.valueOf(count), 7, '0');
    }

    public Date convertToFullTime(Date svcTermDate) {
        Assert.notNull(svcTermDate, "SvcTermDate is null");

        LocalDateTime dateTime = svcTermDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
                .plusDays(1).minus(1, ChronoUnit.SECONDS);

        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public int deleteAddItem(String addItemCode) {
        Assert.notNull(addItemCode, "addItemCode is null");


        return gfnAddMapper.deleteAddItem(addItemCode);
    }
}


