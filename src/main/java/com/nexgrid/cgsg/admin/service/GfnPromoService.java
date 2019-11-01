package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.StatusCode;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.mapper.GfnPromoMapper;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfo;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfoParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class GfnPromoService {

    @Autowired
    private GfnPromoMapper gfnPromoMapper;

    public String insertPromo(GfnPromoInfo promoInfo) {
        Assert.notNull(promoInfo, "promoInfo is null");
        Assert.hasLength(promoInfo.getEmail(), "promoInfo.email is null");
        Assert.notNull(promoInfo.getDueDt(), "promoInfo.duDt is null");

        String promoCode = this.generatePromoCode();

        promoInfo.setPromoCode(promoCode);
        promoInfo.setStatusCd(StatusCode.UNUSED.getCode());
        promoInfo.setGfnId(this.makeGfnId(promoCode));

        gfnPromoMapper.insertPromo(promoInfo);

        return promoInfo.getPromoCode();
    }

    public List<GfnPromoInfo> selectPromoList(GfnPromoInfoParam promoInfo) {
        return gfnPromoMapper.selectPromoList(promoInfo);
    }

    public int updatePromo(GfnPromoInfoParam promoInfo) {

        if (StatusCode.EXPIRATION.getCode().equalsIgnoreCase(promoInfo.getStatusCd())) {
            promoInfo.setStatusCd(StatusCode.UNUSED.getCode());
        }

        return this.updatePromoRaw(promoInfo);
    }

    private GfnPromoInfo selectPromo(String promoCode) {
        List<GfnPromoInfo> gfnPromoInfos = selectPromoList(GfnPromoInfoParam.builder().promoCode(promoCode).build());
        Assert.notNull(gfnPromoInfos, "promoList is null");
        Assert.isTrue(gfnPromoInfos.size() > 0, "promoList is zero");

        return gfnPromoInfos.get(0);
    }

    private int updateGfnMaster(GfnPromoInfoParam promoInfo) {
        GfnPromoInfo gfnPromoInfo = this.selectPromo(promoInfo.getPromoCode());
        return gfnPromoMapper.updateGfnMaster(gfnPromoInfo.getGfnId(), promoInfo.getDueDt());
    }

    public String generatePromoCode() {

        String promoCode;
        int limitLoop = 5;

        do {
            promoCode = RandomStringUtils.randomAlphanumeric(16).toUpperCase();
            Assert.hasLength(promoCode, "promoCode was not made");
            Assert.isTrue(promoCode.length() == 16, "promoCode was not made");
            limitLoop--;
        } while (this.isExistPromoCode(promoCode) && limitLoop > 0);

        if(limitLoop == 0)
            throw new AdminException(SystemStatusCode.INTERNAL_ERROR, "it was exceed a generation promoCode");

        return promoCode;
    }

    private String makeGfnId(String promoCode) {
        return DigestUtils.sha256Hex("PROMO" + promoCode);
    }

    public int deletePromoCode(String mbrId, List<String> promoCodeList) {
        Assert.notNull(promoCodeList, "promoCodeList is null");
        Assert.isTrue(promoCodeList.size() > 0, "promoCodeList size is greater than 0");

        int result = 0;
        for (String promoCode : promoCodeList) {
            result += this.updatePromoRaw(GfnPromoInfoParam.builder()
                    .promoCode(promoCode)
                    .dueDt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .statusCd(StatusCode.EXPIRATION.getCode())
                    .mbrId(mbrId)
                    .build());

        }
        return result;
    }

    private int updatePromoRaw(GfnPromoInfoParam promoInfo) {
        Assert.notNull(promoInfo, "promoInfo is null");
        Assert.hasLength(promoInfo.getPromoCode(), "promoCode is null");
        Assert.notNull(promoInfo.getDueDt(), "dueDt is null");
        Assert.hasLength(promoInfo.getStatusCd(), "status is null");

        this.updateGfnMaster(promoInfo);
        return gfnPromoMapper.updatePromo(promoInfo);
    }

    public boolean existPromoUserInfo(GfnPromoInfoParam promoInfo) {
        List<GfnPromoInfo> gfnPromoInfos = gfnPromoMapper.existPromoUserInfo(promoInfo);

        return gfnPromoInfos.size() > 0 ? true : false;
    }

    public boolean isExistPromoCode(String promoCode) {
        Assert.notNull(promoCode, "promoCode is null");

        List<GfnPromoInfo> gfnPromoInfos = gfnPromoMapper.selectPromoList(GfnPromoInfoParam.builder()
                                                                                           .promoCode(promoCode)
                                                                                           .build());

        return gfnPromoInfos.size() > 0 ? true : false;
    }
}
