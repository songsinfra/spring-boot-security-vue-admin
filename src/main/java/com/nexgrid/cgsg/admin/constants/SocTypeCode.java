package com.nexgrid.cgsg.admin.constants;

public enum SocTypeCode {
    PROD("P"),
    SERVICE("R");

    private String code;

    SocTypeCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
