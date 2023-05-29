package com.github.ksewen.ganyu.configuration.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.ksewen.ganyu.configuration.properties.JwtProperties;
import com.github.ksewen.ganyu.service.JwtService;
import com.github.ksewen.ganyu.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author ksewen
 * @date 10.05.2023 12:24
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final String TOKEN_PREFIX = "Bearer ";

    private final Integer TOKEN_PREFIX_LENGTH = 7;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = this.getToken(request);
        if (StringUtils.hasLength(token)) {
            String username = this.jwtService.extractUsername(token);

            logger.debug("checking authentication " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                boolean isTokenValid = this.tokenService.findByToken(token).map(t -> !t.getExpired() && !t.getExpired())
                        .orElse(false);
                if (isTokenValid && this.jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.debug("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(this.jwtProperties.getReadHeader());
        if (!StringUtils.hasLength(header) || !header.startsWith(this.TOKEN_PREFIX)) {
            return null;
        }
        return header.substring(this.TOKEN_PREFIX_LENGTH);
    }
}
