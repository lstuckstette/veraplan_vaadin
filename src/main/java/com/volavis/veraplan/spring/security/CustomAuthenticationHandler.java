package com.volavis.veraplan.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationHandler.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // TODO: rework login using ajax or something.... https://www.webucator.com/how-to/how-create-login-form-with-ajax.cfm


        JsonReader reader = new JsonReader(new InputStreamReader(request.getInputStream()));
        JsonElement element = new JsonParser().parse(reader);
        JsonObject object = element.getAsJsonObject();

        /*
        JSONParser jsonParser = new JSONParser();
        JSONObject requestBody = null;
        try {
            requestBody = (JSONObject) jsonParser.parse(new InputStreamReader(request.getInputStream()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        /*
        if (requestBody != null) {
            response.getOutputStream().println("{error: \"" + exception.getMessage() + " for request: " + requestBody.toJSONString() + "\"}");
        } else {
            response.getOutputStream().println("{error: \"" + exception.getMessage() + "\"}");
        }
        */

        response.getOutputStream().println("{error: \"" + exception.getMessage() + " for request: " + object.toString() + "\"}");

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        logger.info("login success!~");
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("/");


    }
}
