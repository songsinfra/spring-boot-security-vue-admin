package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnAddInfo {

    @Size(max = 10, message = "addItemCode의 크기가 10보다 큽니다")
    @NotNull
    private String addItemCode;

    @Size(max = 1, message = "addItemType의 크기가 1보다 큽니다")
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

    @Size(max = 4, message = "svcTermNum의 크기가 4보다 큽니다")
    private Integer svcTermNum;

    private Date svcTermDate;

    private String createId;
    private Date createDt;
    private String updateId;
    private Date updateDt;

//    @AssertTrue(message = "나이는 0보다 커야 하며 150보다 작아야 합니다.")
//    public boolean isValidSvcTermType() {
//        return AddItemType.GFN.getType().equalsIgnoreCase(this.svcTermType) ?
//                this.svcTermType == SvcTermType.NONE.getType() :
//                this.svcTermType != SvcTermType.NONE.getType();
//
//    }

}
