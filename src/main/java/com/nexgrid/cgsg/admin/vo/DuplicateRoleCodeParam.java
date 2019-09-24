package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DuplicateRoleCodeParam {
    @Size(max = 10, message = "roleCode의 크기가 10보다 큽니다")
    @NotNull(message = "roleCode의 값이 없습니다.")
    private String roleCode;
}
