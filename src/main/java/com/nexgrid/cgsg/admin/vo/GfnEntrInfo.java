package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GfnEntrInfo {
    private String entrItemCode;
    private String socTypeCode;
    private String prodCd;
    private String entrItemNm;
    private String statusCd;
    private String memo;

    private String createId;
    private Date createDt;
    private String updateId;
    private Date updateDt;
}
