package com.nexgrid.cgsg.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SystemStatusCode {
    LOGIN_SUCCESS("24400000"),
    LOGIN_FAIL_LOCK("44401001"),
    LOGIN_FAIL_LOCK_LEFT_TIME("44401002"),
    NOT_FOUND_USER_ID("44401003"),
    FAIL_LOGIN("44401004"),
    LOGIN_FAIL_LOCKED_USER_ID("44401005"),
    INTERNAL_ERROR("44401006")
    ;

    private String code;
}
