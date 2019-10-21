package com.nexgrid.cgsg.admin.vo;

import com.nexgrid.cgsg.admin.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnAddInfo {

    @Size(max = 10, message = "addItemCode의 크기가 10보다 큽니다")
    private String addItemCode;

    @Size(max = 1, message = "addItemType의 크기가 1보다 큽니다")
    @NotNull(message = "addItemType의 값이 없습니다.")
    @NotEmpty(message = "addItemType의 값이 없습니다.")
    private String addItemType;

    @Size(max = 120, message = "addItemNm의 크기가 120보다 큽니다")
    private String addItemNm;

    private Integer svcSellPrice;
    private Integer svcBasePrice;

    @Size(max = 4000, message = "addItemDetail의 크기가 4000보다 큽니다")
    private String addItemDetail;

    @Size(max = 4000, message = "addItemNotice의 크기가 4000보다 큽니다")
    private String addItemNotice;

    @Size(max = 1, message = "svcTermType의 크기가 1보다 큽니다")
    private String svcTermType;

    @Size(max = 1, message = "svcTermUnit의 크기가 1보다 큽니다")
    private String svcTermUnit;

//    @Max(value = 4, message = "svcTermNum의 크기가 4보다 큽니다")
    private Integer svcTermNum;

    private Date svcTermDate;

    @Size(max = 1, message = "statusCd 의 크기가 1보다 큽니다")
    private String statusCd;

    private String createId;
    private Date createDt;
    private String updateId;
    private Date updateDt;
}
