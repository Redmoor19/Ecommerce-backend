package com.example.gameStore.config;

import com.example.gameStore.dtos.GlobalResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomHttp403ForbiddenEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(403);
        response.setContentType("application/json");

        GlobalResponse<Void> body = new GlobalResponse<>(HttpStatus.FORBIDDEN.value(), "Access denied");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
