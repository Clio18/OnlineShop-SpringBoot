package com.obolonyk.shopboot.security;

import com.obolonyk.shopboot.security.filter.JWTAuthorizationFilter;
import com.obolonyk.shopboot.security.filter.JsonObjectAuthenticationFilter;
import com.obolonyk.shopboot.security.service.JWTUserDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static com.obolonyk.shopboot.security.model.UserPermission.PRODUCT_READ;
import static com.obolonyk.shopboot.security.model.UserPermission.PRODUCT_WRITE;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTSecurity {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AuthSuccessHandler authSuccessHandler;
    private final JWTUserDetailsService jwtUserDetailsService;
    private final String secret;

    public JWTSecurity(AuthSuccessHandler authSuccessHandler, JWTUserDetailsService jwtUserDetailsService, @Value("${security.secret}")String secret) {
        this.authSuccessHandler = authSuccessHandler;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.secret = secret;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests(this::getAuth);

        return http.build();
    }

    @Bean
    public JsonObjectAuthenticationFilter authenticationFilter() {
        JsonObjectAuthenticationFilter filter = new JsonObjectAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authSuccessHandler);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @SneakyThrows
    private void getAuth(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
            auth
                    .antMatchers("/", "/login", "/api/v1/users/registration").permitAll()

                    .antMatchers(HttpMethod.GET, "/api/v1/products/**").hasAuthority(PRODUCT_READ.getPermission())
                    .antMatchers(HttpMethod.POST, "/api/v1/products").hasAuthority(PRODUCT_WRITE.getPermission())
                    .antMatchers(HttpMethod.POST, "/api/v1/products/**").hasAuthority(PRODUCT_READ.getPermission())
                    .antMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAuthority(PRODUCT_WRITE.getPermission())
                    .antMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAuthority(PRODUCT_WRITE.getPermission())

                    .antMatchers("/api/v1/cart/**").hasAuthority(PRODUCT_READ.getPermission())

                    .anyRequest().authenticated()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .addFilter(authenticationFilter())
                    .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUserDetailsService, secret))
                    .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(jwtUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
