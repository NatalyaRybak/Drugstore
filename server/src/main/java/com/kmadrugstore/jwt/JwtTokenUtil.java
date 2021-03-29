package com.kmadrugstore.jwt;

import com.kmadrugstore.entity.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    // 2 hours
    public static final long JWT_TOKEN_VALIDITY_IN_MS = 2 * 60 * 60 * 1000;

    public static final String USER_ID_CLAIM_NAME = "userId";
    public static final String ROLE_CLAIM_NAME = "role";

    @Value("${jwt.secret}")
    private String secret;

    private final UserDetailsService userDetailsService;

    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public int getUserIdFromToken(final String token) {
        return (int) getAllClaimsFromToken(token).get(USER_ID_CLAIM_NAME);
    }

    public String getRoleFromToken(final String token) {
        return (String) getAllClaimsFromToken(token).get(ROLE_CLAIM_NAME);
    }

    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(final String token,
                                   final Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(final String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(final User user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(user.getEmail(), user.getRole().getRole(),
                user.getId());
    }

    private String doGenerateToken(final String subject,
                                   final String userRole, final int userId) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put(ROLE_CLAIM_NAME, userRole);
        claims.put(USER_ID_CLAIM_NAME, userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +
                        JWT_TOKEN_VALIDITY_IN_MS))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

//    public Boolean validateToken(final String token,
//                                 final User user) {
//        try {
//            final String username = getUsernameFromToken(token);
//            return (username.equals(user.getEmail()) && !isTokenExpired
//            (token));
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public boolean validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(final String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(
                getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

}