package com.nexgrid.cgsg.admin.constants;

public enum StatusCode {
    UNUSED("0"),     // 미사용
    USED("1"),       // 사용
    EXPIRATION("2"),  // 만료처리
    TERM_EXPIRATION("3") // 기간만료
    ;

    private String code;

    StatusCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
