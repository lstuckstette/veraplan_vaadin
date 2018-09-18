package com.volavis.veraplan.spring.persistence.service;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.volavis.veraplan.spring.persistence.model.Role;
import com.volavis.veraplan.spring.persistence.model.RoleName;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.repository.RoleRepository;
import com.volavis.veraplan.spring.persistence.repository.UserRepository;
import org.atmosphere.config.service.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class PopulateDemoDatabaseService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;


    private static final Logger logger = LoggerFactory.getLogger(PopulateDemoDatabaseService.class);

    //@Autowired
    public PopulateDemoDatabaseService() {
        passwordEncoder = new BCryptPasswordEncoder();
        //populate();
    }

    @PostConstruct
    public void populate() {
        createRoles();
        createUsers();
    }

    private void createRoles() {
        // Create specific Roles:
        //Role adminRole = new Role(RoleName.ROLE_ADMIN);
        //Role userRole = new Role(RoleName.ROLE_USER);
        //createRole(adminRole, userRole);

        // Create all possible Roles of enum:
        for (RoleName rName : RoleName.values()) {
            createRole(new Role(rName));
        }


    }

    private void createRole(Role... roles) {
        for (Role role : roles) {
            logger.info("role:" + role.getName());
            if (!roleRepository.findByName(role.getName()).isPresent()) { //TODO: nullpointer!
                roleRepository.save(role);
            }
        }

    }

    private void createUsers() {
        createUser("Johnathan Frakes", "jfrakes", "frakes@web.de", "test", RoleName.ROLE_USER);
        createUser("aname", "admin", "admin@admin.de", "password", RoleName.ROLE_ADMIN);
    }

    private void createUser(String name, String username, String email, String password, RoleName rolename) {
        User user = new User(name, username, email, passwordEncoder.encode(password));
        //check existing username/email:
        if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
            return;
        }
        //check Role exists:
        if (roleRepository.findByName(rolename).isPresent()) {
            user.setRoles(Collections.singleton(new Role(rolename)));
            //finally save user.
            userRepository.save(user);
        }


    }
}
