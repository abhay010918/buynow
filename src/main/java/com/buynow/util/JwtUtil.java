package com.buynow.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.buynow.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.algorithm.key}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private String expiryDuration;

    private Algorithm algorithm;
    private long expiryMs;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secret);
        this.expiryMs = Long.parseLong(expiryDuration);
    }

    // ✅ Generate JWT token
    public String generateToken(User user) {
        return JWT.create()
                .withIssuer(issuer)
                .withClaim("name", user.getEmail())
                .withClaim("role", user.getRoles().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryMs))
                .sign(algorithm);
    }

    // ✅ Extract username/email from token
    public String getUsername(String token) {
        return decodeToken(token).getClaim("name").asString();
    }

    // ✅ Extract role from token
    public String getRole(String token) {
        return decodeToken(token).getClaim("role").asString();
    }

    // ✅ Validate token (signature, issuer, expiry, and ownership)
    public boolean isTokenValid(String token, User user) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            String username = decodedJWT.getClaim("name").asString();
            return username.equals(user.getEmail()) && !isTokenExpired(decodedJWT);
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    // ✅ Verify and decode securely
    private DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        return verifier.verify(token);
    }

    // ✅ Decode token (without verification)
    private DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }

    // ✅ Check if token expired
    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        Date expiration = decodedJWT.getExpiresAt();
        return expiration != null && expiration.before(new Date());
    }
}
