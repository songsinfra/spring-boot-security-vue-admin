package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.AddItemType;
import com.nexgrid.cgsg.admin.constants.StatusCode;
import com.nexgrid.cgsg.admin.constants.SvcTermType;
import com.nexgrid.cgsg.admin.constants.SvcTermUnit;
import com.nexgrid.cgsg.admin.mapper.GfnAddMapper;
import com.nexgrid.cgsg.admin.mapper.GfnEntrMapper;
import com.nexgrid.cgsg.admin.utils.StringUtil;
import com.nexgrid.cgsg.admin.vo.GfnAddInfo;
import com.nexgrid.cgsg.admin.vo.GfnMapInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
public class GfnAddService {

    @Autowired
    private GfnAddMapper gfnAddMapper;

    @Autowired
    private GfnEntrMapper gfnEntrMapper;

    public GfnAddInfo selectAddItem(String addItemCode) {
        Assert.hasLength(addItemCode, "addItemCode is null");

        GfnAddInfo gfnAddInfo = gfnAddMapper.selectAddItem(addItemCode);
        Assert.notNull(gfnAddInfo, "not found gfnAddInfo");

        this.convertDdToNewLine(gfnAddInfo);
        return gfnAddInfo;
    }

    public List<GfnAddInfo> selectAddItemList(String statusCd) {
        return gfnAddMapper.selectAddItemList(statusCd);
    }

    public int insertAddItemForGfn(GfnAddInfo gfnAddInfo) {
        this.validateDataForGfn(gfnAddInfo);

        // GFN ID 생성
        gfnAddInfo.setAddItemCode(this.generateAddItemCode());
        this.convertNewLineToDd(gfnAddInfo);

        return this.setAddItemForGfn(info -> gfnAddMapper.insertAddItem((GfnAddInfo)info), gfnAddInfo);
    }

    public int updateAddItemForGfn(GfnAddInfo gfnAddInfo) {
        this.validateDataForGfn(gfnAddInfo);
        Assert.hasLength(gfnAddInfo.getAddItemCode(), "addItemCode is null");

        this.convertNewLineToDd(gfnAddInfo);
        this.processStatusCd(gfnAddInfo);

        return this.setAddItemForGfn(info -> gfnAddMapper.updateAddItem((GfnAddInfo)info), gfnAddInfo);
    }

    private int setAddItemForGfn(Function function, GfnAddInfo gfnAddInfo) {
        if (StringUtils.equalsIgnoreCase(SvcTermType.LIMIT_DATE.getType(), gfnAddInfo.getSvcTermType())) {
            Date fullTime = this.convertToFullTime(gfnAddInfo.getSvcTermDate());
            gfnAddInfo.setSvcTermDate(fullTime);

            //clear unused Data svcTermData
            gfnAddInfo.setSvcTermUnit(null);
            gfnAddInfo.setSvcTermNum(null);
        } else if(StringUtils.equalsIgnoreCase(SvcTermType.AVAILABLE_DATE.getType(), gfnAddInfo.getSvcTermType())){
            gfnAddInfo.setSvcTermDate(null);
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
                            StringUtils.equalsIgnoreCase(SvcTermUnit.Day.getCode(), gfnAddInfo.getSvcTermUnit()) ||
                            StringUtils.equalsIgnoreCase(SvcTermUnit.Hour.getCode(), gfnAddInfo.getSvcTermUnit()),
                    "SvcTermUnit value is only M,D,H Code");

            Assert.notNull(gfnAddInfo.getSvcTermNum(), "svcTermNum is null");
        }
    }

    public int insertAddItemForUcube(GfnAddInfo gfnAddInfo) {
        this.validateDataForUcude(gfnAddInfo);
        this.convertNewLineToDd(gfnAddInfo);

        return gfnAddMapper.insertAddItem(gfnAddInfo);
    }

    public int updateAddItemForUcube(GfnAddInfo gfnAddInfo) {
        this.validateDataForUcude(gfnAddInfo);
        this.convertNewLineToDd(gfnAddInfo);
        this.processStatusCd(gfnAddInfo);

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

    private void processStatusCd(GfnAddInfo gfnAddInfo) {
        if (gfnAddInfo.getStatusCd().equalsIgnoreCase(StatusCode.UNUSED.getCode())) {
            gfnEntrMapper.disableMapItemForAdd(GfnMapInfo.builder()
                    .addItemCode(gfnAddInfo.getAddItemCode())
                    .deleteId(gfnAddInfo.getUpdateId())
                    .build());
        } else if (gfnAddInfo.getStatusCd().equalsIgnoreCase(StatusCode.USED.getCode())) {
            gfnEntrMapper.enableMapItemForAdd(GfnMapInfo.builder()
                    .addItemCode(gfnAddInfo.getAddItemCode())
                    .deleteId(gfnAddInfo.getUpdateId())
                    .build());
        }
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

    private void convertNewLineToDd(GfnAddInfo gfnAddInfo) {
        if (!StringUtils.isEmpty(gfnAddInfo.getAddItemDetail())) {
            gfnAddInfo.setAddItemDetail(StringUtil.convertNewListToDd(gfnAddInfo.getAddItemDetail()));
        }

        if (!StringUtils.isEmpty(gfnAddInfo.getAddItemNotice())) {
            gfnAddInfo.setAddItemNotice(StringUtil.convertNewListToDd(gfnAddInfo.getAddItemNotice()));
        }
    }

    private void convertDdToNewLine(GfnAddInfo gfnAddInfo) {
        if (!StringUtils.isEmpty(gfnAddInfo.getAddItemDetail())) {
            gfnAddInfo.setAddItemDetail(StringUtil.convertDdToNewLine(gfnAddInfo.getAddItemDetail()));
        }

        if (!StringUtils.isEmpty(gfnAddInfo.getAddItemNotice())) {
            gfnAddInfo.setAddItemNotice(StringUtil.convertDdToNewLine(gfnAddInfo.getAddItemNotice()));
        }
    }
}


