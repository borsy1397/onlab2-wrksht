package com.captainborsy.wrksht.service;

import io.jsonwebtoken.SigningKeyResolver;

public interface SecretService {

    SigningKeyResolver getSigningKeyResolver();

    byte[] getHS512SecretBytes();

}
