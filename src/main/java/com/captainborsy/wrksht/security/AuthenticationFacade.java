package com.captainborsy.wrksht.security;

public interface AuthenticationFacade {
    AuthUserDetails getCurrentUserFromContext();
}
