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
@NotNull
@Builder
public class RoleMstParam {
    @Size(max = 10, message = "roleCode의 크기가 10보다 큽니다")
    private String roleCode;

    @Size(max = 1, message = "useYn의 크기가 1보다 큽니다")
    private String useYn;

    @Size(max = 8, message = "menuId의 크기가 8보다 큽니다")
    private String menuId;
}
