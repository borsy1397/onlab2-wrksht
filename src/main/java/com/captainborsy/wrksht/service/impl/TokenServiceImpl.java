package com.captainborsy.wrksht.service.impl;


import com.captainborsy.wrksht.errorhandling.exception.InvalidTokenException;
import com.captainborsy.wrksht.model.Role;
import com.captainborsy.wrksht.service.SecretService;
import com.captainborsy.wrksht.service.TokenService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    public static final String USER_ID_CLAIM = "userid";
    public static final String ROLE_CLAIM = "role";
    private static final String TOKEN_ISSUER = "captainborsy";
    private static final String CONFIRM_EMAIL_TOKEN_SUBJECT = "confirmemail";
    private static final String PASSWORD_SETTING_TOKEN_SUBJECT = "passwordsetting";
    private static final String ACCESS_TOKEN_SUBJECT = "accesstoken";
    private static final String REFRESH_TOKEN_SUBJECT = "refreshtoken";
    private static final int CONFIRM_EMAIL_TOKEN_TTL = 5 * 24 * 60 * 60 * 1000; // 5 days
    private static final int PASSWORD_SETTING_TOKEN_TTL = 5 * 24 * 60 * 60 * 1000; // 5 days
    private static final int ACCESS_TOKEN_TTL = 30 * 60 * 1000; // 30 minutes
    private static final int REFRESH_TOKEN_TTL = 365; // 365 days

    private SecretService secretService;

    @Autowired
    public TokenServiceImpl(SecretService secretService) {
        this.secretService = secretService;
    }

    @Override
    public String generateAccessToken(String email, String id, Role role) {
        return generateToken(email, id, role, ACCESS_TOKEN_SUBJECT, new Date(System.currentTimeMillis() + ACCESS_TOKEN_TTL));
    }


    @Override
    public String generateRefreshToken(String email, String id, Role role) {
        return generateToken(email, id, role, REFRESH_TOKEN_SUBJECT, new Date(Instant.now().plus(REFRESH_TOKEN_TTL, ChronoUnit.DAYS).toEpochMilli()));
    }

    @Override
    public String validateRefreshToken(String refreshToken) {
        return validateToken(REFRESH_TOKEN_SUBJECT, refreshToken);
    }

    private String generateToken(String email, String id, Role role, String subject, Date expirationDate) {
        return Jwts.builder()
                .setSubject(subject)
                .setAudience(email)
                .setIssuer(TOKEN_ISSUER)
                .claim(ROLE_CLAIM, role)
                .claim(USER_ID_CLAIM, id)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretService.getHS512SecretBytes())
                .compact();
    }

    private String validateToken(String subject, String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .requireIssuer(TOKEN_ISSUER)
                    .requireSubject(subject)
                    .setSigningKeyResolver(secretService.getSigningKeyResolver())
                    .parseClaimsJws(token);

            return claimsJws.getBody().getAudience();
        } catch (JwtException e) {
            throw new InvalidTokenException(e);
        }
    }

}
