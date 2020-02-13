package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class AcceptTosInfo {
    private String gfnId;
    private String tosNo;
    private String acceptCd;
    private String updateDt;
}
