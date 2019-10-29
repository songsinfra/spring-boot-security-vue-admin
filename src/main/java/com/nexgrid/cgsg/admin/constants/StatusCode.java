package com.nexgrid.cgsg.admin.constants;

public enum StatusCode {
    UNUSED("0"),
    USED("1"),
    EXPIRATION("2")
    ;

    private String code;

    StatusCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
