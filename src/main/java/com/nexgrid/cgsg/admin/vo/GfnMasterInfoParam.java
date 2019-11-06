package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnMasterInfoParam {
    private String entrItemCode;
    private String addItemCode;

    @Pattern(regexp = "^$|\\d{8}", message = "날짜형식이 맞지 않습니다.")
    private String createStartDt;

    @Pattern(regexp = "^$|\\d{8}", message = "날짜형식이 맞지 않습니다.")
    private String createEndDt;
    private String subNo;
    private String ctn;
}
