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
    private String month;
    private int freeSum;
    private int paidSum;
    private int totalSum;
}
