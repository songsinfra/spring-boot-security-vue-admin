package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnPromoInfoParam {
    @Size(max = 16, message = "promoCode 의 크기가 16보다 큽니다")
    private String promoCode;

    @Size(max = 50, message = "email 의 크기가 50보다 큽니다")
    private String email;

    @Size(max = 10, message = "dueDt 의 크기가 10보다 큽니다")
    private String dueDt;

    @Size(max = 1, message = "statusCd 의 크기가 1보다 큽니다")
    private String statusCd;

    @Size(max = 20, message = "contactNo 의 크기가 20보다 큽니다")
    private String contactNo;

    private String mbrId;
    private String name;

    private List<String> promoCodeList;
}
