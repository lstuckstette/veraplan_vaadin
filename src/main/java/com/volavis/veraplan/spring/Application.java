package com.volavis.veraplan.spring;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * The entry point of the Spring Boot application.
 */

@SpringBootApplication(scanBasePackageClasses = {Application.class, SecurityConfig.class, PopulateDemoDatabaseService.class, DashboardView.class})
@EnableScheduling //for messaging test..
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    public Application(@Autowired PopulateDemoDatabaseService populateDemoDatabaseService) {
        //Initialize DB:
        populateDemoDatabaseService.populate();

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);


    }


}
