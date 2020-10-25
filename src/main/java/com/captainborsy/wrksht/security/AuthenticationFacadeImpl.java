package com.captainborsy.wrksht.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    @Override
    public AuthUserDetails getCurrentUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return (AuthUserDetails) authentication.getPrincipal();
        } else {
            return null;
        }
    }
}
