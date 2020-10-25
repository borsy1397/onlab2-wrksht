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
    public static final WrkshtErrors INVALID_OPERATION = new WrkshtErrors("INVALID_OPERATION", 1003);
    public static final WrkshtErrors UNAVAILABLE = new WrkshtErrors("UNAVAILABLE", 4800);
    public static final WrkshtErrors INVALID_PASSWORD = new WrkshtErrors("INVALID_PASSWORD", 1005);
    public static final WrkshtErrors UNKNOWN = new WrkshtErrors("UNKNOWN", 1007);
    public static final WrkshtErrors ENTITY_NOT_FOUND = new WrkshtErrors("ENTITY_NOT_FOUND", 1008);
    public static final WrkshtErrors UNAUTHORIZED = new WrkshtErrors("UNAUTHORIZED", 1009);
    public static final WrkshtErrors PRODUCT_EXISTS = new WrkshtErrors("PRODUCT_EXISTS", 1011);
    public static final WrkshtErrors wrksht_TIME_ERROR = new WrkshtErrors("wrksht_TIME_ERROR", 1012);
    public static final WrkshtErrors NOT_BELONGS_TO_DEPOT = new WrkshtErrors("NOT_BELONGS_TO_DEPOT", 1013);
    public static final WrkshtErrors NO_ENOUGH_QUANTITY = new WrkshtErrors("NO_ENOUGH_QUANTITY", 1014);
    public static final WrkshtErrors TRUCK_IS_ON_WAY = new WrkshtErrors("TRUCK_IS_ON_WAY", 1014);
    public static final WrkshtErrors NEGATIVE_QUANTITY = new WrkshtErrors("NEGATIVE_QUANTITY", 1015);
    public static final WrkshtErrors USER_INFORMATION_NOT_EXISTS = new WrkshtErrors("USER_INFORMATION_NOT_EXISTS", 1022);
}