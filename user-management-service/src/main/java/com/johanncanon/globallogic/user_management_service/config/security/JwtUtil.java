package com.johanncanon.globallogic.user_management_service.config.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value( "${app.jwt.secret}" )
    private String jwtSecret;

    @Value( "${app.jwt.expiration}" )
    private int jwtExpirationMs;

    private SecretKey getSingKey() {
        return Keys.hmacShaKeyFor( jwtSecret.getBytes() );
    }

    public String generateToken( String username ) {
        return Jwts.builder()
            .setSubject( username )
            .setIssuedAt( new Date() )
            .setExpiration( new Date( System.currentTimeMillis() + jwtExpirationMs ) )
            .signWith( getSingKey() )
            .compact();
    }

    public String getUsernameFromToken( String token ) {
        return Jwts.parserBuilder()
            .setSigningKey( getSingKey() )
                .build()
                .parseClaimsJws( token )
                .getBody()
                .getSubject();
    }

    public boolean validateToken( String token ) {
        try {
            Jwts.parserBuilder()
                .setSigningKey( getSingKey() )
                .build()
                .parseClaimsJws( token );
            return true;
        } catch ( JwtException | IllegalArgumentException e ) {
            return false;
        }
    }


}
