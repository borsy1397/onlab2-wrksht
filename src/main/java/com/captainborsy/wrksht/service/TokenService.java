package com.captainborsy.wrksht.service;


import com.captainborsy.wrksht.model.Role;

public interface TokenService {

    String generateAccessToken(String email, String id, Role role);

    String generateRefreshToken(String email, String id, Role role);

    String validateRefreshToken(String refreshToken);
}
