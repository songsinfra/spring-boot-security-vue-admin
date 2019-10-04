package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntrMppingListParam {
    @Size(max = 10, message = "entrItemCode 의 크기가 10보다 큽니다")
    @NotNull(message = "entrItemCode 의 값이 없습니다.")
    @NotEmpty(message = "entrItemCode 의 값이 없습니다.")
    private String entrItemCode;

    @NotNull(message = "addItemCodeList의 값이 없습니다.")
    private List<String> addItemCodeList;
}
