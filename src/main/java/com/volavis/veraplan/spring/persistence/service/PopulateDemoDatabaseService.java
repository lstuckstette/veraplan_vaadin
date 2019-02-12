package com.volavis.veraplan.spring.persistence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.volavis.veraplan.spring.persistence.entities.RoleName;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
import com.volavis.veraplan.spring.persistence.entities.ressources.Room;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeConstraint;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.exception.EntityAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class PopulateDemoDatabaseService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private PlanratingService planratingService;

    @Autowired
    private TimeConstraintService timeConstraintService;

    private static final Logger logger = LoggerFactory.getLogger(PopulateDemoDatabaseService.class);

    public void populate() {
        roleService.createAllRoles();
        createDummyUsers();
        createDummyChannel();
//        createTimeSlots();
//        createBuilding();
//        createRooms();

        //clear entered userdata:
        planratingService.deleteAll();
        timeConstraintService.removeAll();

        logger.info("Finished populating Database.");
    }

    /**
     * JSON-File contains 10 Timeslots with corresponding start-/end-times; needs to be looped through each weekday...
     * creating 70 TimeSlots (10 each day)
     */
    private void createTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        try {
            Path jsonPath = ResourceUtils.getFile("classpath:database_dummydata/timeslots.json").toPath();
            BufferedReader reader = Files.newBufferedReader(jsonPath);
            Type type = new TypeToken<List<TimeSlot>>() {
            }.getType();
            Gson gson = new Gson();
            List<TimeSlot> timeSlotList = gson.fromJson(reader, type);

            for (TimeSlot t : timeSlotList) {
                for (int i = 1; i < 8; i++) {
                    TimeSlot newTimeSlot = new TimeSlot();
                    newTimeSlot.setEndTime(t.getEndTime());
                    newTimeSlot.setStartTime(t.getStartTime());
                    newTimeSlot.setTimeSlotIndex(t.getTimeSlotIndex());
                    newTimeSlot.setWeekday(i);
                    timeSlots.add(newTimeSlot);
                }
            }

            timeSlotService.saveAll(timeSlots);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (EntityAlreadyExistsException e) {
            //this is fine!
        }

        logger.info("Populate: Created" + timeSlots.size() + " TimeSlots.");

    }

    private void createBuilding() {
        Building mainBuilding = new Building();
        mainBuilding.setName("Main Building");
        mainBuilding.setShortName("MB");
        try {
            buildingService.saveBuilding(mainBuilding);
        } catch (EntityAlreadyExistsException e) {
            //this is fine!
        }
        logger.info("Populate: Created Building");
    }

    private void createRooms() {

        Room r = new Room();
//        r.setBuilding();

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

            userService.createUser("Hubert", "Schmidt", "hschmidt", "heissenberg@volavis.com", "hschmidt", RoleName.ROLE_ADMIN);
            userService.createUser("Franziska", "Meier", "fmeier", "busse@volavis.de", "fmeier", RoleName.ROLE_ADMIN);
//            userService.createUser("The", "Admin", "admin", "admin@admin.de", "admin", RoleName.ROLE_ADMIN);
//            userService.createUser("Vorname", "Nachname", "test", "test@test.de", "test", RoleName.ROLE_ADMIN);




            //Read dummy-file and create all Users:
//            try {
//                byte[] dummyData = Files.readAllBytes(ResourceUtils.getFile("classpath:database_dummydata/users.json").toPath());
//                ObjectMapper objectMapper = new ObjectMapper();
//                List<User> dummyUsers = objectMapper.readValue(dummyData, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
//                dummyUsers.forEach((user -> userService.createUser(user, RoleName.ROLE_USER)));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } catch (EntityAlreadyExistsException e) {
            //this is fine!
        }

    }


}
