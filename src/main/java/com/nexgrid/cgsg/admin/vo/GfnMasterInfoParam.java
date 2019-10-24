package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnMasterInfoParam {
    private String entrItemCode;
    private String addItemCode;
    private String createStartDt;
    private String createEndDt;
    private String subNo;
    private String ctn;
}
