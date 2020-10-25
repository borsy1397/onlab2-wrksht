package com.captainborsy.wrksht.errorhandling.exception;


import com.captainborsy.wrksht.errorhandling.WrkshtException;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;

public class CredentialException extends WrkshtException {
    public CredentialException(String msg, WrkshtErrors wrkshtError) {
        super(msg, wrkshtError);
    }
}
