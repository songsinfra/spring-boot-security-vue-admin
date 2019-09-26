package com.nexgrid.cgsg.admin.constants;

public enum ProdCode {
    MOBILE("LZP0000001"),
    INTERNET("LZP0000601"),
    IPTV("LZP0000701");

    private String code;

    ProdCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
