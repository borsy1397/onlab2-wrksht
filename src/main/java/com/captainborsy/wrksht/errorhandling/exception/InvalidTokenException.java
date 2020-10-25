package com.captainborsy.wrksht.errorhandling.exception;


import com.captainborsy.wrksht.errorhandling.WrkshtException;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;

public class InvalidTokenException extends WrkshtException {

    public InvalidTokenException(String msg, WrkshtErrors wrkshtError) {
        super(msg, wrkshtError);
    }

    public InvalidTokenException(Throwable e) {
        super(e);
    }
}
