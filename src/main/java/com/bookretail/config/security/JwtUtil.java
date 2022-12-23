package com.bookretail.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.bookretail.enums.EClientId;
import com.bookretail.enums.EExpiration;
import com.bookretail.enums.ERole;
import com.bookretail.model.User;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Logger logger;
    private final EClientId clientIds;
    private final Map<String, Long> clientAndExpirations;

    public JwtUtil(Logger logger, EExpiration expiration, EClientId clientIds) {
        this.logger = logger;
        this.clientIds = clientIds;
        clientAndExpirations = Map.of(
                clientIds.getWebClientId(), expiration.getJwtExpirationWeb(),
                clientIds.getMobileClientId(), expiration.getJwtExpirationMobile()
        );
    }

    @Value("${application.jwt.secret}")
    private String jwtSecret;

    public String generateAccessToken(@NotNull User user) {
        return generateAccessToken(user, clientIds.getMobileClientId());

    }

    public String generateAccessToken(@NotNull User user, String clientId) {
        return generateAccessToken(user, clientAndExpirations.get(clientId),
                Map.of(
                        "roleId", user.getRole()
                )
        );
    }

    private String generateAccessToken(@NotNull User user, long expirationMs, Map<String, Object> claims) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationMs);

        return Jwts.builder()
                .setId(String.valueOf(user.getId()))
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserRole(@NotNull String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token.contains(" ") ? token.split(" ")[1].trim() : token)
                .getBody();

        return ERole.stringValueOf(claims.get("roleId", Integer.class));
    }

    public Long getUserId(@NotNull String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token.contains(" ") ? token.split(" ")[1].trim() : token)
                .getBody();

        return Long.parseLong(claims.getId());
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validateToken(String token) {
        try { //If token matches with our secret
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            logger.error("Invalid JWT Token - {}", ex.getMessage());
        }
        return false;
    }
}
