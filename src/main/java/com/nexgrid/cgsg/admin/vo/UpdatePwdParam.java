package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePwdParam {
    @Size(max = 10, message = "mbrId의 크기가 10보다 큽니다")
    @NotNull(message = "mbrId의 값이 없습니다.")
    private String mbrId;

    @Size(max = 512, message = "mbrPw의 크기가 512보다 큽니다")
    @NotNull(message = "변경할 비밀번호 값이 없습니다.")
    private String mbrNewPw;
}
