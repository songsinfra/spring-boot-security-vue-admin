package com.nexgrid.cgsg.admin.constants;

public enum StatusCode {
    USE("0"),
    UNUSED("1");

    private String code;

    StatusCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
