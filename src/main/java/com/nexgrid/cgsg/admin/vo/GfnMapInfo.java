package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnMapInfo {
    private String addItemCode;
    private String entrItemCode;
    private String createId;
    private Date createDt;
    private String statusCd;
}
