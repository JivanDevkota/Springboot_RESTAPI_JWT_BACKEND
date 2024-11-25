package com.nsu.jwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final String secret=
            "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";

//    private SecretKey key= Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public Date getExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username) // This adds the "sub" claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24-hour validity
                .signWith(getSignKey())
                .compact();
    }

    private static Key getSignKey(){
        byte[] decode = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(decode);
    }

    public  String extractUsernameFromToken(String token) {
        String username = extractClaim(token, Claims::getSubject);
        System.out.println("Token: " + token);
        System.out.println("Extracted Username: " + username);
        return username;
    }

    public  <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
       final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public  Claims extractAllClaims(String token) {
        return
                Jwts.parserBuilder()
                        .setSigningKey(getSignKey()).build()
                        .parseClaimsJws(token).getBody();
    }

}
