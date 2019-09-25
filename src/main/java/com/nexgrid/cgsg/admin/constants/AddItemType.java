package com.nexgrid.cgsg.admin.constants;

public enum AddItemType {
    GFN("G"),
    UCUBE("U");

    String type;
    AddItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
