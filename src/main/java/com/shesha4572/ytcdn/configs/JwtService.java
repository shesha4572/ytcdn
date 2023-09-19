package com.shesha4572.ytcdn.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String JWT_SECRET_KEY;

    @Autowired
    public JwtService(@Value("${JWT_SECRET}") String jwtSecret){
        JWT_SECRET_KEY = jwtSecret;
    }
    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject);
    }

    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(this.getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwt(Map<String , Object> extraClaims , UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1296000000))
                .signWith(this.getSignInKey() , SignatureAlgorithm.HS256)
                .compact();

    }

    public String generateJwt(UserDetails userDetails){
        return this.generateJwt(new HashMap<>(), userDetails);
    }

    public Boolean isTokenValid(String token , UserDetails userDetails){
        return (this.extractUsername(token).equals(userDetails.getUsername()) && !this.isTokenExpired(token));
    }

    private Date extractExpiration(String token){
        return this.extractClaim(token , Claims::getExpiration);
    }
    private boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }
}
