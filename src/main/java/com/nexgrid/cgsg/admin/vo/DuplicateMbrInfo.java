package com.nexgrid.cgsg.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateMbrInfo {

    @Size(max = 10, message = "mbrId의 크기가 10보다 큽니다")
    private String mbrId;

    @Size(max = 20, message = "전화번호의 크기가 20보다 큽니다")
    private String tel;
}
