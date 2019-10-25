package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GfnJoinStatInfo {
    private String joinType;
    private String lastYearData;
    private String prevMonthData;
    private String lastMonthData;
    private String currentMonthData;
}
