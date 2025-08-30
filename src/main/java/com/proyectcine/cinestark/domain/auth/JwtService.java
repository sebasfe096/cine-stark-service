package com.proyectcine.cinestark.domain.auth;

import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtService {
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    private final long EXPIRATION = 86400000; // 24h

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().getName())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        String tokenOnly = cleanToken(token);
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(tokenOnly)
                .getBody();
    }

    public void validateToken(String token) {
        String tokenOnly = cleanToken(token);
        try {
            Claims claims = extractAllClaims(tokenOnly);
            log.info("claims -> {} " , claims);
            /*if(!claims.getExpiration().before(new Date()))
                throw new BusinessException("El token ha expirado", HttpStatus.UNAUTHORIZED);*/
        } catch (JwtException e) {
            log.error("Error jwt", e.getMessage());
            log.error("Error jwt", e.getStackTrace());
            throw new BusinessException("Token JWT invalido", HttpStatus.BAD_REQUEST);
        }
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }


    public Integer extractUserId(String token) {
        return extractAllClaims(token).get("userId", Integer.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String tokenOnly = cleanToken(token);
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(tokenOnly)
                .getBody();

        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }

    public String cleanToken(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return token.trim();
    }
}
