package com.nexgrid.cgsg.admin.vo;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class GfnJoinStatInfo {
    private String signupDate;
    private String freeUserCnt;
    private String paidUserCnt;
    private String totUserCnt;
}
