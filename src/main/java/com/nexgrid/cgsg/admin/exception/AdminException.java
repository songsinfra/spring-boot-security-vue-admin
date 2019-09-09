package com.nexgrid.cgsg.admin.exception;

import lombok.Data;

@Data
public class AdminException extends RuntimeException {
    private String code;


    public AdminException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AdminException(String message) {
        super(message);
    }

    public AdminException(String message, Throwable cause) {
        super(message, cause);
    }
}