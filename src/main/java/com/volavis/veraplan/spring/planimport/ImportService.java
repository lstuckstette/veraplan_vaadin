package com.volavis.veraplan.spring.planimport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.ressources.Assignment;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.planimport.model.ImportAssignment;
import com.volavis.veraplan.spring.planimport.model.ImportPlan;
import com.volavis.veraplan.spring.planimport.model.ImportTimeslot;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class ImportService {

//    @Autowired
//    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);
    private List<ImportPlan> plans = new ArrayList<>();
    private Map<String, String> userToTeacher;
    private Map<String, String> teacherToUsername;


    @PostConstruct
    public void setup() {

        //Read json files:
        for (int i = 1; i <= 3; i++) {
            ImportPlan iPlan = null;
            try {
                Path jsonPath = ResourceUtils.getFile("classpath:database_dummydata/generated-plan-" + i + ".json").toPath();
                BufferedReader reader = Files.newBufferedReader(jsonPath);
                Type type = new TypeToken<ImportPlan>() {
                }.getType();
                Gson gson = new Gson();
                iPlan = gson.fromJson(reader, type);
                logger.info("READ " + iPlan.getAssignments().size() + " Mock-Assignments!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.plans.add(iPlan);
        }


        //setup teacher <-> user mapping
        userToTeacher = new HashMap<>();
        teacherToUsername = new HashMap<>();

        this.setUserTeacherMapping("Lehrer 1", "admin");
        this.setUserTeacherMapping("Lehrer 2", "test");
    }

    private void setUserTeacherMapping(String teacher, String username) {
        userToTeacher.put(username, teacher);
        teacherToUsername.put(teacher, username);
    }

    public Optional<String> getUsernameFromTeacher(String teacher) {
        if (teacherToUsername.containsKey(teacher)) {
            return Optional.of(teacherToUsername.get(teacher));
        }
        return Optional.empty();
    }

    public Optional<String> getTeacherFromUsername(String username) {
        if (userToTeacher.containsKey(username)) {
            return Optional.of(userToTeacher.get(username));
        }
        return Optional.empty();
    }

    //returns List of Assignments containing each mapped teacherIDs Assignments
    public List<ImportAssignment> getMockTeacherPlan(int planIndex, User... users) {


        List<ImportAssignment> result = new ArrayList<>();
        for (User user : users) {
            if (userToTeacher.containsKey(user.getUsername()) && planIndex < plans.size()) {

                String teacherID = userToTeacher.get(user.getUsername());
                result.addAll(plans.get(planIndex).getAssignments().stream().filter(assignment -> assignment.getTeacher().getId().equals(teacherID)).collect(Collectors.toList()));
            }
        }
        return result;
    }

    public Optional<ImportAssignment> checkCollision(int planIndex, ImportAssignment source, ImportTimeslot target) {

        if (planIndex >= plans.size()) {
            return Optional.empty();
        }

        return this.plans.get(planIndex).getAssignments().stream().filter(assignment -> {
            //TODO:
            return assignment.getTaughtClass().equals(source.getTaughtClass()) &&
                    assignment.getTimeSlot().equals(target);
        }).findAny();

    }

}
