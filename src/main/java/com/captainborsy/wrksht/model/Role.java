package com.captainborsy.wrksht.model;


import com.captainborsy.wrksht.security.AuthoritiesConstants;

public enum Role {
    ADMIN(AuthoritiesConstants.ROLE_ADMIN),
    WORKER(AuthoritiesConstants.ROLE_WORKER);

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
