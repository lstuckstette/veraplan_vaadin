package com.volavis.veraplan.spring.security;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomUsernamePasswordAuthenticationFilter.class);

    private String jsonUsername;
    private String jsonPassword;

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = null;

        if ("application/json".equals(request.getHeader("Content-Type"))) {
            username = this.jsonUsername;
        } else {
            username = super.obtainUsername(request);
        }
        return username;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = null;

        if ("application/json".equals(request.getHeader("Content-Type"))) {
            password = this.jsonPassword;
        } else {
            password = super.obtainPassword(request);
        }
        return password;
    }

    //Used to handle JSON based Login instead of plain XHR-post
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        if ("application/json".equals(request.getHeader("Content-Type"))) {

            JsonReader reader = null;
            try {
                reader = new JsonReader(new InputStreamReader(request.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                throw new InternalAuthenticationServiceException("Non-JSON authentication request body");
            }
            JsonElement element = new JsonParser().parse(reader);
            JsonObject object = element.getAsJsonObject();

            if (object.has(this.getUsernameParameter()) && object.has(this.getPasswordParameter())) {

                this.jsonPassword = object.get(this.getPasswordParameter()).getAsString();
                this.jsonUsername = object.get(this.getPasswordParameter()).getAsString();

            } else {
                throw new InternalAuthenticationServiceException("Malformed authentication request body");
            }


        }
        return super.attemptAuthentication(request, response);
    }
}
