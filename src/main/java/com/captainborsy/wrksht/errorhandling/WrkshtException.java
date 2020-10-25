package com.captainborsy.wrksht.errorhandling;

import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;

public class WrkshtException extends RuntimeException {
    private WrkshtErrors error;

    public WrkshtException() {
    }

    public WrkshtException(String message, WrkshtErrors error) {
        super(message);
        this.error = error;
    }

    public WrkshtException(String message, WrkshtErrors error, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public WrkshtErrors getError() {
        return this.error;
    }

    public WrkshtException(String message) {
        super(message);
    }

    public WrkshtException(WrkshtErrors error) {
        this.error = error;
    }

    public WrkshtException(Throwable e) {
        super(e);
    }

}
