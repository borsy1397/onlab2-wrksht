package com.captainborsy.wrksht.errorhandling.exception;

import com.captainborsy.wrksht.errorhandling.WrkshtException;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;

public class NoUserInContextException extends WrkshtException {
    public NoUserInContextException(String msg, WrkshtErrors wrkshtError) {
        super(msg, wrkshtError);
    }
}
