package com.captainborsy.wrksht.errorhandling.domain;

import java.io.Serializable;

public class ErrorResponseDTO implements Serializable {

    private String errorMessage;
    private String errorKey;
    private Integer errorCode;

    public ErrorResponseDTO(WrkshtErrors wrkshtError, String errorMessage) {
        this.errorCode = wrkshtError.getCode();
        this.errorKey = wrkshtError.getKey();
        this.errorMessage = errorMessage;
    }
}
