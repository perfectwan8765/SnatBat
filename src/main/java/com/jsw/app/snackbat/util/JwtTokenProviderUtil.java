package com.jsw.app.snackbat.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProviderUtil {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private long tokenValidMiliSecond = 1000L * 60 * 60; // token 유지 시간 1 hour

    public String generateToken (String name) {
        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
                   .setId(name)
                   .setIssuedAt(now)
                   .setExpiration(new Date(now.getTime() + tokenValidMiliSecond))
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }

    public String getUserNameFromJwt (String jwt) {
        return this.getClaims(jwt).getBody().getId();
    }

    
    public boolean validateToken (String jwt) {
        return this.getClaims(jwt) != null;
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throw ex;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw ex;
        }
    }
}