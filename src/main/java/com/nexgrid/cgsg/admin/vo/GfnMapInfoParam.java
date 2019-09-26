package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GfnMapInfoParam {
    @Size(max = 10, message = "addItemCode 의 크기가 10보다 큽니다")
    private String addItemCode;

    @Size(max = 10, message = "entrItemCode 의 크기가 10보다 큽니다")
    private String entrItemCode;

    private String createId;
    private Date createDt;
    private String statusCd;
}
