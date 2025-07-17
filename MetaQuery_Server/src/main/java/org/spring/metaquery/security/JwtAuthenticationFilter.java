package org.spring.metaquery.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter is a filter that checks for a JWT token in the request cookies,
 * validates it, and sets the authentication in the security context if valid.
 * It extends OncePerRequestFilter to ensure it is executed once per request.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Extracts the JWT token from the request cookies.
     *
     * @param request The HTTP request containing cookies.
     * @return The JWT token if found, otherwise null.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Determines if the filter should not process the request.
     * This filter does not process requests to the login and signup endpoints.
     *
     * @param request The HTTP request to check.
     * @return true if the request should not be filtered, false otherwise.
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/login") || path.startsWith("/api/auth/signup");
    }

    /**
     * Filters the request to check for a valid JWT token.
     * If a valid token is found, it sets the authentication in the security context.
     *
     * @param request  The HTTP request to filter.
     * @param response The HTTP response.
     * @param filterChain The filter chain to continue processing the request.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if (token != null && jwtService.validateToken(token)) {
            String email = jwtService.extractUsername(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
