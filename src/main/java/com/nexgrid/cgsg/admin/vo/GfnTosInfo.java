package com.nexgrid.cgsg.admin.vo;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnTosInfo {
    private String tosNo;
    private String tosNm;
    private String dipOrder;
    private String tosManYn;
    private String tosType;
    private String tosWebUrl;
    private String tosAppUrl;
    private String updateDt;
    private String statusCode;
    private String isAgree;


    @Getter @Setter
    static public class GfnTosInfoParams{
        @NotNull(message = "gfnId is null")
        @NotEmpty(message = "gfnId is empty")
        private String gfnId;
    }


    @Getter @Setter
    static public class updateAcceptTosListParams{
        @NotNull(message = "gfnId is null")
        @NotEmpty(message = "gfnId is empty")
        private String gfnId;

        private String agreeList;
    }
}
