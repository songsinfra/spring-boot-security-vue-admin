package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnMasterInfo {
    private String gfnId;
    private String ctn;
    private String subNo;
    private String prodCd;
    private String entrItemCode;
    private String entrItemNm;
    private String addItemCode;
    private String addItemNm;
    private String ctnStusCode;
    private String createDt;
    private String lastLoginDt;
}
