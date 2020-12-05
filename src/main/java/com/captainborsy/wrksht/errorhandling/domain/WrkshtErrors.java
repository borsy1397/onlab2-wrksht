package com.captainborsy.wrksht.errorhandling.domain;

import java.io.Serializable;

public class WrkshtErrors implements Serializable {

    protected final String key;
    protected final int code;

    private WrkshtErrors(String key, int code) {
        this.key = key;
        this.code = code;
    }

    public String getKey() {
        return this.key;
    }

    public int getCode() {
        return this.code;
    }

    public static final WrkshtErrors CONFLICT = new WrkshtErrors("CONFLICT", 1000);
    public static final WrkshtErrors ACCESS_DENIED = new WrkshtErrors("ACCESS_DENIED", 1002);
    public static final WrkshtErrors INVALID_PASSWORD = new WrkshtErrors("INVALID_PASSWORD", 1005);
    public static final WrkshtErrors UNKNOWN = new WrkshtErrors("UNKNOWN", 1007);
    public static final WrkshtErrors ENTITY_NOT_FOUND = new WrkshtErrors("ENTITY_NOT_FOUND", 1008);
    public static final WrkshtErrors UNAUTHORIZED = new WrkshtErrors("UNAUTHORIZED", 1009);
    public static final WrkshtErrors ADMIN_DELETE_ERROR = new WrkshtErrors("ADMIN_DELETE_ERROR", 1023);
    public static final WrkshtErrors USER_ALREADY_LOGGED_IN = new WrkshtErrors("USER_ALREADY_LOGGED_IN", 1024);
    public static final WrkshtErrors DELETED_USER = new WrkshtErrors("DELETED_USER", 1025);
    public static final WrkshtErrors STATUS_CHANGE_ERROR = new WrkshtErrors("STATUS_CHANGE_ERROR", 1026);
    public static final WrkshtErrors PARSE_ERROR = new WrkshtErrors("PARSE_ERROR", 1027);
    public static final WrkshtErrors DELETED_WORKFLOW = new WrkshtErrors("DELETED_WORKFLOW", 1025);
    public static final WrkshtErrors DEADLINE_ERROR = new WrkshtErrors("DEADLINE_ERROR", 1026);
    public static final WrkshtErrors WORKSHEET_ALREADY_FINISHED = new WrkshtErrors("WORKSHEET_ALREADY_FINISHED", 1027);
    public static final WrkshtErrors WORKFLOW_ALREADY_FINISHED = new WrkshtErrors("WORKFLOW_ALREADY_FINISHED", 1028);
    public static final WrkshtErrors WORKFLOW_IN_PROGRESS = new WrkshtErrors("WORKFLOW_IN_PROGRESS", 1029);
    public static final WrkshtErrors EXPORT_ERROR = new WrkshtErrors("EXPORT_ERROR", 1030);
}
