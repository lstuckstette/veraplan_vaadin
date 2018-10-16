package com.volavis.veraplan.spring.security;


import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if ("application/json".equals(request.getHeader("Content-Type"))) {
            JSONParser jsonParser = new JSONParser();

            try {
                JSONObject requestBody = (JSONObject) jsonParser.parse(new InputStreamReader(request.getInputStream()));

                if (!requestBody.isEmpty()
                        && requestBody.containsKey(this.getUsernameParameter())
                        && requestBody.containsKey(this.getPasswordParameter())) {
                    //return new UsernamePasswordAuthenticationToken(requestBody.get("username"), requestBody.get("password"));
                    this.jsonPassword = (String) requestBody.get(this.getPasswordParameter());
                    this.jsonUsername = (String) requestBody.get(this.getUsernameParameter());
                    logger.info("got JSON: " + jsonUsername + " " + jsonPassword);
                } else {
                    throw new InternalAuthenticationServiceException("Malformed authentication request body");
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw new InternalAuthenticationServiceException("Failed to fetch authentication request body");
            } catch (ParseException e) {
                e.printStackTrace();
                throw new InternalAuthenticationServiceException("Failed to parse authentication request body");
            }
        }
        return super.attemptAuthentication(request, response);
    }
}
