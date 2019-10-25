package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GfnJoinStatDetailInfo {
    private String signupDate;
    private String freeUserCnt;
    private String paidUserCnt;
    private String totUserCnt;
}
