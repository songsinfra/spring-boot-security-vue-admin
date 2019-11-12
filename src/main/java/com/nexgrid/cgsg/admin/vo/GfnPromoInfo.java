package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnPromoInfo {
    private String promoCode;

    @Size(max = 50, message = "email 의 크기가 50보다 큽니다")
    private String email;
    private String createDt;

    @Size(max = 10, message = "dueDt 의 크기가 10보다 큽니다")
    private String dueDt;
    private String mbrId;

    @Size(max = 1, message = "statusCd 의 크기가 1보다 큽니다")
    private String statusCd;

    @Size(max = 20, message = "contactNo 의 크기가 20보다 큽니다")
    private String contactNo;
    private String gfnId;
    private String name;
}
