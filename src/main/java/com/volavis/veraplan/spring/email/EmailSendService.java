package com.volavis.veraplan.spring.email;

import com.volavis.veraplan.spring.persistence.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService {

    @Autowired
    JavaMailSender mailSender;


    @Async
    public void sendCollaborationInvite(User recipient, User sender) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient.getEmail());
        message.setSubject("Veraplan - Kollaborationsanfrage von " + sender.getFirst_name() + "!");
        message.setText("Guten Tag " + recipient.getFirst_name() + " " + recipient.getLast_name() + "! \n \n " +
                "Der Nutzer " + sender.getFirst_name() + " " + sender.getLast_name() + " hat Sie eingeladen kollaborativ ihre Stundenpläne zu bearbeiten! \n" +
                "Bitte rufen Sie hierzu folgenden Link auf: \n \n" +
                "http://localhost:8080/plancollab?collaboration=" + sender.getId()+
                "\n \n Das Team von Veraplan wünscht ihnen noch einen schönen Tag!");
        mailSender.send(message);
    }
}
