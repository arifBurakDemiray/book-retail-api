package com.bookretail.config.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.bookretail.util.service.UserDetailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailService service;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UserDetailService service) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null) {
            chain.doFilter(request, response);

            return;
        }

        if (!header.startsWith("Bearer ")) {
            chain.doFilter(request, response);

            logger.warn(
                    String.format(
                            "Authentication with no Bearer token attempted (%s).",
                            request.getRequestURI()
                    )
            );

            return;
        }

        var authHeader = header.split(" ");
        if (authHeader.length != 2) {
            chain.doFilter(request, response);

            logger.warn(
                    String.format(
                            "Authentication attempted with malformed authorization header (%s).",
                            request.getRequestURI()
                    )
            );

            return;
        }

        String token = authHeader[1].trim();

        var isValid = jwtUtil.validateToken(token);
        if (!isValid) {
            request.setAttribute("expired", "yes");
            chain.doFilter(request, response);

            logger.warn(
                    String.format(
                            "Authentication attempted with not a valid token (%s).",
                            request.getRequestURI()
                    )
            );

            return;
        }

        UserDetails userDetails = service.loadUserByUsername(jwtUtil.getUsername(token));

        if (userDetails == null) {
            chain.doFilter(request, response);

            logger.warn(
                    String.format(
                            "Authentication attempted with non-exist user (%s).",
                            request.getRequestURI()
                    )
            );

            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);

    }
}
