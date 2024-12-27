package com.project.spendly2.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("{spendly.jwt.secret.key}")
    public  String SECRET_KEY ;

    public String generateToken(UserDetails user) {

        return Jwts.builder()

                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000 ))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    protected Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    protected <T> T extractClaims(String token, Function<Claims, T> extract) {
        return extract.apply(extractAllClaims(token));
    }


    public String extractEmailAddressFromToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){

        final String email = extractEmailAddressFromToken(token);
        return (email.equals(userDetails.getUsername()));
    }


    public Boolean isExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }



}
