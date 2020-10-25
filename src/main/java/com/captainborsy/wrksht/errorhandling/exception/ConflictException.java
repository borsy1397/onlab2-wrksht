package com.captainborsy.wrksht.errorhandling.exception;


import com.captainborsy.wrksht.errorhandling.WrkshtException;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;

public class ConflictException extends WrkshtException {
    public ConflictException(String msg, WrkshtErrors wrkshtError) {
        super(msg, wrkshtError);
    }
}
