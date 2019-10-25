package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GfnJoinStatDetailInfoParam {

    @Size(max = 8, message = "startDt의 크기가 8보다 큽니다")
    @NotNull(message = "startDt의 값이 없습니다.")
    @NotEmpty(message = "startDt의 값이 없습니다.")
    private String startDt;

    @Size(max = 8, message = "endDt의 크기가 8보다 큽니다")
    @NotNull(message = "endDt의 값이 없습니다.")
    @NotEmpty(message = "endDt의 값이 없습니다.")
    private String endDt;
}
