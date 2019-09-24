package com.nexgrid.cgsg.admin.vo;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthInfo {
	@Size(max = 10, message = "roleCode의 크기가 10보다 큽니다")
	@NotNull(message = "roleCode의 값이 없습니다.")
	private String roleCode;

	@Size(max = 30, message = "codeNm의 크기가 10보다 큽니다")
	private String codeNm;

	@Size(max = 2, message = "managerYn의 크기가 2보다 큽니다")
	private String managerYn;

	@Size(max = 1, message = "useYn의 크기가 10보다 큽니다")
	@NotNull(message = "useYn의 값이 없습니다.")
	private String useYn;

	@Size(max = 8, message = "menuId의 크기가 8보다 큽니다")
	private String menuId;
}
