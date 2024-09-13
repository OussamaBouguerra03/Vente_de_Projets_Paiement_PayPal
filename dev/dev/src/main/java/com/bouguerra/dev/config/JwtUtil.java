package com.bouguerra.dev.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.bouguerra.dev.models.Role;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey; // Clé secrète lue depuis la configuration

    // Ajoutez un log pour vérifier la clé secrète
    private void logSecretKey() {
        System.out.println("JWT Secret Key: " + secretKey);
    }

    public String generateToken(String username, Role role) {
        logSecretKey(); // Assurez-vous que la clé secrète est correcte
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    public Role extractRole(String token) {
        String role = (String) parseClaims(token).get("role");
        return Role.valueOf(role);
    }
    public String refreshToken(String token) {
        // Extraire les informations du token existant
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    
        // Créer un nouveau token avec les mêmes informations
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 heure
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }
    
}