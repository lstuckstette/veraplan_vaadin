package com.volavis.veraplan.spring.persistence.entities.communication;

import com.volavis.veraplan.spring.persistence.audit.DateAudit;
import com.volavis.veraplan.spring.persistence.entities.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "message")
public class Message extends DateAudit {
    //TODO: implement

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private User sender;

    @NotBlank
    private User recipient;

    @Size(max=80)
    private String subject;

    @NotBlank
    @Size(max=2000)
    private String body;

    public Message(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
