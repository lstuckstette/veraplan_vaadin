package com.volavis.veraplan.spring;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

@Service
public class UserSession {

    private boolean loggedIn;

    private int counter;

    public UserSession() {
        this.loggedIn = true;
        counter = 0;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getMessage() {
        return "Button was clicked " + counter + " times.";
    }
}
