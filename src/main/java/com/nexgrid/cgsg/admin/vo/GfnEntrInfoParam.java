package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GfnEntrInfoParam {

    @Size(max = 10, message = "entrItemCode 의 크기가 10보다 큽니다")
    private String entrItemCode;

    @Size(max = 1, message = "socTypeCode 의 크기가 1보다 큽니다")
    private String socTypeCode;

    @Size(max = 20, message = "prodCd 의 크기가 20보다 큽니다")
    private String prodCd;

    @Size(max = 120, message = "entrItemNm 의 크기가 120보다 큽니다")
    private String entrItemNm;

    @Size(max = 1, message = "statusCd 의 크기가 1보다 큽니다")
    private String statusCd;

    @Size(max = 2048, message = "memo 의 크기가 2018보다 큽니다")
    private String memo;

    private String createId;
    private Date createDt;
    private String updateId;
    private Date updateDt;
}
