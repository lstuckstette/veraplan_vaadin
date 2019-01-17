package com.volavis.veraplan.spring.security;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.stream.Collectors;

@Component
public class CustomAuthenticationHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationHandler.class);

    @Autowired
    RequestCache requestCache;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        logger.info("login error: " + exception.getMessage());
        String requestBody = new BufferedReader(new InputStreamReader(request.getInputStream())).lines().collect(Collectors.joining());

        //requestBody.replaceAll("\"", "'");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        response.getOutputStream().println("{error: \"" + exception.getMessage() + " for request: " + requestBody + "\"}");

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        logger.info("login success!~");

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        response.setStatus(HttpStatus.OK.value());

        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();

            if(redirectUrl.contains("communication")){
                response.sendRedirect("/");
            } else {
                logger.info("redirecting to:" + redirectUrl);
                response.sendRedirect(redirectUrl);
            }

        } else {

            response.sendRedirect("/");
        }


    }
}
