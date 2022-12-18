package com.obolonyk.shopboot.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.net.HttpHeaders;
import com.obolonyk.shopboot.security.service.JWTUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JWTUserDetailsService jwtUserDetailsService;
    private final String secret;


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JWTUserDetailsService jwtUserDetailsService,
                                  String secret) {
        super(authenticationManager);
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken auth = getAuthentication(request);
        if(auth==null){
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        String login = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        if(login==null) return null;
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(login);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                null,
                userDetails.getAuthorities());
    }

}
