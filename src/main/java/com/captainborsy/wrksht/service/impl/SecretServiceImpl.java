package com.captainborsy.wrksht.service.impl;

import com.captainborsy.wrksht.service.SecretService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Service
public class SecretServiceImpl implements SecretService {

    private Map<String, String> secrets = new HashMap<>();

    private SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter() {
        @Override
        public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
            return TextCodec.BASE64.decode(secrets.get(header.getAlgorithm()));
        }
    };

    @Autowired
    public SecretServiceImpl(@Value("${wrksht.security.token.secret}") String secret) {
        Map<String, String> secretMap = new HashMap<>();
        secretMap.put(SignatureAlgorithm.HS512.getValue(), secret);
        setSecrets(secretMap);
    }

    @PostConstruct
    private void setup() {
        if (secrets.isEmpty()) {
            refreshSecrets();
        }
    }

    @Override
    public SigningKeyResolver getSigningKeyResolver() {
        return signingKeyResolver;
    }

    @Override
    public byte[] getHS512SecretBytes() {
        return TextCodec.BASE64.decode(secrets.get(SignatureAlgorithm.HS512.getValue()));
    }

    private final synchronized void setSecrets(Map<String, String> secrets) {
        Assert.notNull(secrets);
        Assert.hasText(secrets.get(SignatureAlgorithm.HS512.getValue()));

        this.secrets = secrets;
    }

    private Map<String, String> refreshSecrets() {
        SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS512);
        secrets.put(SignatureAlgorithm.HS512.getValue(), TextCodec.BASE64.encode(key.getEncoded()));
        return secrets;
    }
}

