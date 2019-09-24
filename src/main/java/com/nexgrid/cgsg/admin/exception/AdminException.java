package com.nexgrid.cgsg.admin.exception;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.AuthenticationException;

@Data
@EqualsAndHashCode
public class AdminException extends AuthenticationException {
    private SystemStatusCode code;


    public AdminException(SystemStatusCode code, String message) {
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