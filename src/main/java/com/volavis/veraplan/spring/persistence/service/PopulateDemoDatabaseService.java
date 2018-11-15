package com.volavis.veraplan.spring.persistence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.volavis.veraplan.spring.persistence.model.RoleName;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.exception.EntityAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
public class PopulateDemoDatabaseService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ChannelService channelService;

    private static final Logger logger = LoggerFactory.getLogger(PopulateDemoDatabaseService.class);

    public void populate() {
        roleService.createAllRoles();
        createDummyUsers();
        createDummyChannel();
        logger.info("Populated Demo Database.");
    }

    private void createDummyChannel() {
        //contains all users
        try {
            channelService.createChannel(userService.getAllUsers(), "1337");
        } catch (EntityAlreadyExistsException e) {
            //this is fine.
        }
    }

    private void createDummyUsers() {
        try {
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
        } catch (EntityAlreadyExistsException e) {
            //this is fine!
        }

    }


}
