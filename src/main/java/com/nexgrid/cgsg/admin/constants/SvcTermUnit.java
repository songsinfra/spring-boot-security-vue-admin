package com.nexgrid.cgsg.admin.constants;

public enum SvcTermUnit {
    Month("M"),
    Day("D"),
    Hour("H"),
    ;

    private String code;

    SvcTermUnit(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
