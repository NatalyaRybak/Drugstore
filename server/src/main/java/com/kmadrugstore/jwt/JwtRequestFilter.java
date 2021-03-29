package com.kmadrugstore.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res,
                         final   FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenUtil.resolveToken(req);
        if (token != null && jwtTokenUtil.validateToken(token)) {
            // attempt to authenticate user by his token
            Authentication auth = jwtTokenUtil.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(req, res);
    }
}
