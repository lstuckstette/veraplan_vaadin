package com.volavis.veraplan.spring.persistence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
    }

    @PostConstruct
    public void populate() {
        createRoles();
        createUsers();
        logger.info("Populated Demo Database.");
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
            if (!roleRepository.findByName(role.getName()).isPresent()) {
                roleRepository.saveAndFlush(role);
            }
        }

    }

    private void createUsers() {
        createUser("The", "Admin", "admin", "admin@admin.de", "admin", RoleName.ROLE_ADMIN);
        createUser("Lukas", "Stuck", "test", "test@test.de", "test", RoleName.ROLE_ADMIN);

        //Read dummy-file to byte []
        try {
            byte[] dummyData = Files.readAllBytes(ResourceUtils.getFile("classpath:database_dummydata/MOCK_DATA.json").toPath());
            ObjectMapper objectMapper = new ObjectMapper();
            List<User> dummyUsers = objectMapper.readValue(dummyData, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
            dummyUsers.forEach((user -> createUser(user,RoleName.ROLE_USER)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createUser(User user, RoleName... rolenames) {
        createUser(
                user.getFirst_name(),
                user.getLast_name(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                rolenames
        );
    }

    private void createUser(String firstName, String lastName, String username, String email, String password, RoleName... rolenames) {

        //check existing username/email:
        if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
            return;
        }

        User user = new User(firstName, lastName, username, email, passwordEncoder.encode(password));

        //Create Role Set:
        ArrayList<Role> roles = new ArrayList<>();
        for (RoleName rname : rolenames) {
            roleRepository.findByName(rname).ifPresent(role -> roles.add(role));
        }
        //Create User if Roles not empty:
        if (!roles.isEmpty()) {
            user.setRoles(new HashSet<>(roles));
            User result = userRepository.save(user);

        }
    }
}
