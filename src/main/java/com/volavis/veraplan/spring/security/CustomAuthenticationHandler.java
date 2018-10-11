package com.volavis.veraplan.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.volavis.veraplan.spring.persistence.service.PopulateDemoDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationHandler.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // TODO: rework login using ajax or something.... https://www.webucator.com/how-to/how-create-login-form-with-ajax.cfm
        response.setHeader("login-error", exception.getMessage());
        response.sendRedirect("/landing");
    }
}
