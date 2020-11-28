package com.captainborsy.wrksht.errorhandling.exception;

import com.captainborsy.wrksht.errorhandling.WrkshtException;
import com.captainborsy.wrksht.errorhandling.domain.WrkshtErrors;

public class WorkflowStatusChangingException extends WrkshtException {
    public WorkflowStatusChangingException(String msg, WrkshtErrors wrkshtError) {
        super(msg, wrkshtError);
    }
}