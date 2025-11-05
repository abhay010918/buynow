package com.buynow.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
    public void PostConstruct() {
        algorithm = Algorithm.HMAC256(secret);// Initialize the algorithm
        expiryMs = Long.parseLong(expiryDuration);
    }

    public String generateToken(User user) {
        return JWT.create()
                .withClaim("name", user.getEmail())// Add custom claims
                .withClaim("role", user.getRoles().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryMs)) // Set expiration time
                .withIssuer(issuer) // Add the issuer
                .sign(algorithm); // Sign the token with the algorithm
    }

    public String getUsername(String token){
        DecodedJWT verify = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return verify.getClaim("name").asString();
    }

    public String getRole(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
        return decodedJWT.getClaim("role").asString();
    }
}
