package com.obolonyk.shopboot.security;

import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.security.util.JwtUtils;
import com.obolonyk.shopboot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService jwtUserService;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = jwtUserService.getByLogin(principal.getUsername());
        String token = jwtUtils.createJwt(user.getLogin());
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write("{\"token\": \""+token+"\"}");
    }
}
