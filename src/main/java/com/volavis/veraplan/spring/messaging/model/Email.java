package com.volavis.veraplan.spring.messaging.model;

import com.volavis.veraplan.spring.persistence.entities.User;

import java.io.Serializable;

public class Email implements Serializable {

    private User sender;

    private User recipient;

    private String subject;

    private String body;

    public Email() {
    }

    public Email(User sender, User recipient, String subject, String body) {
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
