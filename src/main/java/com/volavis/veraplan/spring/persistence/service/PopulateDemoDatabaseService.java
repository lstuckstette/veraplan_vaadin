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
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(PopulateDemoDatabaseService.class);

    public void populate() {
        roleService.createAllRoles();
        createDummyUsers();
        logger.info("Populated Demo Database.");
    }

    private void createDummyUsers() {
        userService.createUser("The", "Admin", "admin", "admin@admin.de", "admin", RoleName.ROLE_ADMIN);
        userService.createUser("Lukas", "Stuck", "test", "test@test.de", "test", RoleName.ROLE_ADMIN);

        //Read dummy-file and create all Users:
        try {
            byte[] dummyData = Files.readAllBytes(ResourceUtils.getFile("classpath:database_dummydata/MOCK_DATA.json").toPath());
            ObjectMapper objectMapper = new ObjectMapper();
            List<User> dummyUsers = objectMapper.readValue(dummyData, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
            dummyUsers.forEach((user -> userService.createUser(user, RoleName.ROLE_USER)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
