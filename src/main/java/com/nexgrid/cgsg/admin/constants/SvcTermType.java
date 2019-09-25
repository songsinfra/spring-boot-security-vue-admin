package com.nexgrid.cgsg.admin.constants;

public enum SvcTermType {
    NONE("0"),
    AVAILABLE_DATE("1"),
    LIMIT_DATE("2");

    private String type;
    SvcTermType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
