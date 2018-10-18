package com.volavis.veraplan.spring;

import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.repository.UserRepository;
import com.volavis.veraplan.spring.persistence.service.PopulateDemoDatabaseService;
import com.volavis.veraplan.spring.configuration.SecurityConfig;
import com.volavis.veraplan.spring.views.DashboardView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The entry point of the Spring Boot application.
 */
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EnableJpaAuditing
@EntityScan(basePackageClasses = {User.class})
@SpringBootApplication(scanBasePackageClasses = {Application.class,SecurityConfig.class, PopulateDemoDatabaseService.class, DashboardView.class})
public class Application { //TODO: copy bakery Application.java

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }


}
