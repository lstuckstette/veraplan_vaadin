package com.volavis.veraplan.spring;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.volavis.veraplan.spring.messaging.model.Email;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.repository.UserRepository;
import com.volavis.veraplan.spring.persistence.service.PopulateDemoDatabaseService;
import com.volavis.veraplan.spring.configuration.SecurityConfig;
import com.volavis.veraplan.spring.views.DashboardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;

/**
 * The entry point of the Spring Boot application.
 */
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EnableJpaAuditing
@EntityScan(basePackageClasses = {User.class})
@SpringBootApplication(scanBasePackageClasses = {Application.class, SecurityConfig.class, PopulateDemoDatabaseService.class, DashboardView.class})
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private String channel = "derp";

    public Application(@Autowired PopulateDemoDatabaseService populateDemoDatabaseService, @Autowired JmsTemplate template) {
        populateDemoDatabaseService.populate();
        Email email = new Email();
        email.setBody("Email: derpyderp!");
        template.convertAndSend("derp", email);
    }

    @JmsListener(destination = "derp/{channel}")
    public void receiveMessage(Email email, @Header("message_type") String messageType) {
        logger.info(email.getBody());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }


}
