package com.springRemind.spring_remind.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private long tokenValidMilisecond = 1000L * 60 * 60;
    private final UserDetailsService userDetailsService;

    public SecretKey getKey()
    {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String userPk, List<String> roles) {
        Map<String, List<String>> claims = new HashMap<>();
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .subject(userPk)
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidMilisecond))
                .signWith(getKey())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().verifyWith(getKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String jwtToken) {
        try {
             Jws<Claims> claims = Jwts.parser().verifyWith(getKey()).build()
                    .parseSignedClaims(jwtToken);
             return !claims.getPayload().getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }
    }
}
