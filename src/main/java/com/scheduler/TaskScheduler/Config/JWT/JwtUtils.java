package com.scheduler.TaskScheduler.Config.JWT;

import com.scheduler.TaskScheduler.Model.Role;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.sessionTime}")
    private long sessionTimeInSeconds;

    public String createToken(String username, Set<Role> roles) {
        logger.info("Creating token");

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));

        Date now = Date
                .from(LocalDateTime
                        .now()
                        .atZone(ZoneId
                                .systemDefault())
                        .toInstant());
        Date validTime = Date
                .from(LocalDateTime
                        .now()
                        .plusSeconds(sessionTimeInSeconds)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private List<String> getRoleNames(Set<Role> roles) {
        logger.info("GetRoleNames method called");

        return roles.stream()
                .map(Role::name)
                .collect(Collectors.toList());
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        logger.info("Trying to validate token");

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                logger.warn("Token is expired");
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            logger.info("Jwt token is invalid");
            return false;
        }
    }
}
