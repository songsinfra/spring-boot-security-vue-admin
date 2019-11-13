package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.constants.StatusCode;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfo;
import com.nexgrid.cgsg.admin.vo.GfnPromoInfoParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GfnPromoServiceTest extends BaseServiceTest {

    @Autowired
    private GfnPromoService promoService;

    private String EMAIL = "test999999@email.com";
    private String promoCode1;
    private String promoCode2;
    private String CONTACT_NO;

    private GfnPromoInfo.GfnPromoInfoBuilder getPromoInfo() {
        return GfnPromoInfo.builder()
                .email("fda@fdas.soc")
                .contactNo("01012341234")
                .dueDt("20191231")
                .mbrId("test1")
                .statusCd(StatusCode.UNUSED.getCode())
                ;
    }

    @Before
    public void setUp() {
        CONTACT_NO = "01099991111";
        promoCode1 = promoService.insertPromo(getPromoInfo()
                .email(EMAIL)
                .contactNo(CONTACT_NO)
                .build());

        promoCode2 = promoService.insertPromo(getPromoInfo()
                .email("dumy@mail.com")
                .build());
    }

    @Test
    public void insertPromo() {
        GfnPromoInfo promoInfo = getPromoInfo().build();

        String promoCode = promoService.insertPromo(promoInfo);
        System.out.println("promoCode1 = " + promoCode);

        assertThat(promoCode.length()).isEqualTo(16);
        assertThat(promoInfo.getGfnId()).isNotNull();
    }

    @Test
    public void insertPromo_email_null() {
        assertException(IllegalArgumentException.class, "promoInfo.email is null");
        GfnPromoInfo promoInfo = getPromoInfo()
                .email("")
                .build();

        promoService.insertPromo(promoInfo);
    }

    @Test
    public void insertPromo_dueDt_null() {
        assertException(IllegalArgumentException.class, "promoInfo.duDt is null");
        GfnPromoInfo promoInfo = getPromoInfo()
                .dueDt(null)
                .build();

        promoService.insertPromo(promoInfo);
    }


    @Test
    public void selectPromoList() {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder()
                .build();

        List<GfnPromoInfo> gfnPromoInfos = promoService.selectPromoList(promoInfo);

        assertThat(gfnPromoInfos).size().isGreaterThan(2);
    }

    @Test
    public void existPromoUserInfo() {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder()
                .email(EMAIL)
                .contactNo(CONTACT_NO)
                .build();

        boolean result = promoService.existPromoUserInfo(promoInfo);

        assertThat(result).isTrue();
    }

    @Test
    public void existPromo_EMAIL() {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder()
                .email(EMAIL)
                .build();

        boolean result = promoService.existPromoUserInfo(promoInfo);

        assertThat(result).isTrue();
    }

    @Test
    public void existPromo_EMAIL_invalid() {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder()
                .email(EMAIL+"111")
                .build();

        boolean result = promoService.existPromoUserInfo(promoInfo);

        assertThat(result).isFalse();
    }


    @Test
    public void selectPromoList_where_email() {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder()
                .email(EMAIL)
                .build();

        List<GfnPromoInfo> gfnPromoInfos = promoService.selectPromoList(promoInfo);

        assertThat(gfnPromoInfos).size().isEqualTo(1);
    }

    @Test
    public void updatePromo_used() {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder()
                .promoCode(this.promoCode1)
                .dueDt("20200101")
                .mbrId("test1")
                .statusCd(StatusCode.USED.getCode())
                .build();

        promoService.updatePromo(promoInfo);

        List<GfnPromoInfo> gfnPromoInfos = promoService.selectPromoList(GfnPromoInfoParam.builder()
                .email(EMAIL)
                .build());

        assertThat(gfnPromoInfos.get(0).getDueDt()).isEqualTo(promoInfo.getDueDt().replace("235959", ""));
    }

    @Test
    public void updatePromo_사용기간만료_업데이트() {
        GfnPromoInfoParam promoInfo = GfnPromoInfoParam.builder()
                .promoCode(this.promoCode1)
                .dueDt("20200101")
                .mbrId("test1")
                .statusCd(StatusCode.EXPIRATION.getCode())
                .build();

        promoService.updatePromo(promoInfo);

        List<GfnPromoInfo> gfnPromoInfos = promoService.selectPromoList(GfnPromoInfoParam.builder()
                .email(EMAIL)
                .build());

        assertThat(gfnPromoInfos.get(0).getDueDt()).isEqualTo(promoInfo.getDueDt().replace("235959", ""));
        assertThat(gfnPromoInfos.get(0).getStatusCd()).isEqualTo(StatusCode.UNUSED.getCode());
    }

    @Test
    public void generatePromocode() {
        String promoCode = RandomStringUtils.randomAlphanumeric(16);
        System.out.println("s = " + promoCode);

        String sha256Hex = DigestUtils.sha256Hex("PROMO" + promoCode);
        System.out.println("sha256Hex = " + sha256Hex);
    }

    @Test
    public void expirePromoCode() {
        String mbrId = "ADMIN";
        List<String> promoCodeList = Arrays.asList(promoCode1, promoCode2);

        int result = promoService.expirePromoCode(mbrId, promoCodeList);

        assertThat(result).isEqualTo(2);
    }

    @Test
    public void expirePromoCode_promoCode_empty() {
        assertException(IllegalArgumentException.class, "promoCodeList size is 0");

        String mbrId = "ADMIN";
        List<String> promoCodeList = Arrays.asList();

        promoService.expirePromoCode(mbrId, promoCodeList);
    }

    @Test
    public void expirePromoCode_promoCode_null() {
        assertException(IllegalArgumentException.class, "promoCodeList is null");

        String mbrId = "ADMIN";
        List<String> promoCodeList = null;

        promoService.expirePromoCode(mbrId, promoCodeList);
    }

    @Test
    public void deletePromoCode() {
        List<String> promoCodeList = Arrays.asList(promoCode1, promoCode2);

        int result = promoService.deletePromoCode(promoCodeList);

        assertThat(result).isEqualTo(2);
    }

    @Test
    public void deletePromoCode_promoCodeList_null() {
        assertException(IllegalArgumentException.class, "promoCodeList is null");
        List<String> promoCodeList = null;

        promoService.deletePromoCode(promoCodeList);
    }

    @Test
    public void deletePromoCode_promoCodeList_size_0() {
        assertException(IllegalArgumentException.class, "promoCodeList size is 0");
        List<String> promoCodeList = Arrays.asList();

        promoService.deletePromoCode(promoCodeList);
    }


    @Test
    public void checkExistPromoCode() {
        String promoCode = promoCode1;
        boolean existPromoCode = promoService.isExistPromoCode(promoCode);
        assertThat(existPromoCode).isTrue();
    }
}