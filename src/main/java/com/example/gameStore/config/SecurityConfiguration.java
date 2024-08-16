package com.example.gameStore.config;

import com.example.gameStore.enums.UserRole;
import com.example.gameStore.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("api/v1/auth/**").permitAll()
                        .requestMatchers("api/v1/games/all/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("api/v1/games/keys/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "api/v1/games/**").permitAll()
                        .requestMatchers("api/v1/games/reviews/**").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                        .requestMatchers("api/v1/auth/verify/**").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                        .requestMatchers("api/v1/games/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("api/v1/users/me/**").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                        .requestMatchers("api/v1/users/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("api/v1/orders/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("api/v1/extended-orders").hasAuthority(UserRole.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint());
                    exceptionHandling.accessDeniedHandler(customAccessDeniedHandler());
                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
