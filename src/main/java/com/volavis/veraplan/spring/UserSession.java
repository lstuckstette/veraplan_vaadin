package com.volavis.veraplan.spring;

import java.time.LocalTime;
import org.springframework.stereotype.Service;

@Service
public class UserSession {

    private boolean loggedIn;

    public UserSession(){
        this.loggedIn = true;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getMessage() {
        return "Button was clicked at " + LocalTime.now();
    }
}
