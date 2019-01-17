package com.volavis.veraplan.spring.planimport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.ressources.Assignment;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.planimport.model.ImportAssignment;
import com.volavis.veraplan.spring.planimport.model.ImportPlan;
import com.volavis.veraplan.spring.planimport.model.ImportTeacher;
import com.volavis.veraplan.spring.planimport.model.ImportTimeslot;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.map.MultiKeyMap;
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

    private final static int DEFAULTPLAN = 0;

    private ImportPlan personalSource;
    private ImportPlan schmidtPlan;
    private ImportPlan meierPlan;
    private ImportPlan schmidtFinalPlan;
    private ImportPlan meierFinalPlan;

    @PostConstruct
    public void setup() {


        //Read generated plans through json files:
        for (int i = 1; i <= 3; i++) {
            ImportPlan iPlan = null;
            try {
                Path jsonPath = ResourceUtils.getFile("classpath:database_dummydata/generated-plan-" + i + ".json").toPath();
                BufferedReader reader = Files.newBufferedReader(jsonPath);
                Type type = new TypeToken<ImportPlan>() {
                }.getType();
                Gson gson = new Gson();
                iPlan = gson.fromJson(reader, type);
//                logger.info("READ " + iPlan.getAssignments().size() + " Mock-Assignments!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.plans.add(iPlan);
        }

        //read plan used for personal mapping:

        try {
            Path jsonPath = ResourceUtils.getFile("classpath:database_dummydata/generated-personal-source.json").toPath();
            BufferedReader reader = Files.newBufferedReader(jsonPath);
            Type type = new TypeToken<ImportPlan>() {
            }.getType();
            Gson gson = new Gson();
            personalSource = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setup teacher <-> user mapping
        userToTeacher = new HashMap<>();
        teacherToUsername = new HashMap<>();

        this.setUserTeacherMapping("Lehrer 1", "hschmidt");
        this.setUserTeacherMapping("Lehrer 3", "fmeier");
//        this.setUserTeacherMapping("Lehrer 3", "admin");
//        this.setUserTeacherMapping("Lehrer 4", "test");

        //Custom plan:
        generatePersonalPlans();

    }

    private void generatePersonalPlans() {


        int[][] schmidtAssignmentMatrix =
                {{1, 3, 70, 7, 52, 58, 0},   {2, 4, 50, 8, 53, 84, 0},    {0, 12, 0, 0, 55, 85, 0}, {10, 0, 26, 31, 56, 86, 0},  {11, 14, 27, 0, 0, 0, 75}};
        int[][] meierAssignmentMatrix =
                {{0, 43, 28, 40, 79, 88, 0}, {15, 20, 29, 41, 80, 37, 0}, {17, 0, 0, 54, 0, 0, 0},  {18, 22, 47, 42, 82, 34, 0}, {19, 23, 0, 51, 83, 36, 0}};

        int[][] schmidtFinalAssignmentMatrix =
                {{0, 3, 70, 7, 52, 58, 0},   {2, 4, 50, 8, 53, 84, 0},    {1, 12, 0, 75, 55, 85, 0}, {10, 0, 26, 31, 56, 86, 0},  {11, 14, 27, 0, 0, 0, 0}};
        int[][] meierFinalAssignmentMatrix =
                {{17, 43, 28, 40, 79, 88, 0}, {15, 20, 29, 41, 80, 37, 0}, {0, 0, 0, 0, 0, 0, 0},    {18, 22, 47, 42, 82, 34, 0}, {19, 23, 0, 51, 83, 36, 75}};

        schmidtPlan = generatePersonalPlan(schmidtAssignmentMatrix, "hschmidt");
        meierPlan = generatePersonalPlan(meierAssignmentMatrix, "fmeier");
        schmidtFinalPlan = generatePersonalPlan(schmidtFinalAssignmentMatrix, "hschmidt");
        meierFinalPlan = generatePersonalPlan(meierFinalAssignmentMatrix, "fmeier");


//        MultiKeyMap<Integer, Boolean> schmidtFreistunden = new MultiKeyMap<>();
//        schmidtFreistunden.put(1, 7, true);
//        schmidtFreistunden.put(2, 7, true);
//        schmidtFreistunden.put(3, 1, true);
//        schmidtFreistunden.put(3, 3, true);
//        schmidtFreistunden.put(3, 4, true);
//        schmidtFreistunden.put(3, 7, true);
//        schmidtFreistunden.put(4, 2, true);
//        schmidtFreistunden.put(4, 7, true);
//        schmidtFreistunden.put(5, 4, true);
//        schmidtFreistunden.put(5, 5, true);
//        schmidtFreistunden.put(5, 6, true);
//        schmidtPlan = generatePlan(schmidtFreistunden, "hschmidt", 0);
//
//        MultiKeyMap<Integer, Boolean> meierFreistunden = new MultiKeyMap<>();
//        meierFreistunden.put(1, 1, true);
//        meierFreistunden.put(1, 7, true);
//        meierFreistunden.put(2, 7, true);
//        meierFreistunden.put(3, 2, true);
//        meierFreistunden.put(3, 3, true);
//        meierFreistunden.put(3, 5, true);
//        meierFreistunden.put(3, 6, true);
//        meierFreistunden.put(3, 7, true);
//        meierFreistunden.put(4, 7, true);
//        meierFreistunden.put(5, 3, true);
//        meierFreistunden.put(5, 7, true);
//        meierPlan = generatePlan(meierFreistunden, "fmeier", 35);
    }


    public ImportPlan getPersonalPlan(User user, boolean isFinal) {


        if (user.getUsername().equals("hschmidt")) {
            if (isFinal) {
                return schmidtFinalPlan;
            }
            return schmidtPlan;
        }


        if (user.getUsername().equals("fmeier")) {
            if (isFinal) {
                return meierFinalPlan;
            }
            return meierPlan;

        }
        return new ImportPlan();
    }

    //{{1, 3, 5, 7, 52, 58, 0}, {2, 4, 50, 8, 53, 84, 0}, {0, 12, 0, 0, 55, 85, 0}, {10, 0, 26, 31, 56, 86, 0}, {11, 14, 27, 0, 0, 0, 75, 0}};
    private ImportPlan generatePersonalPlan(int[][] data, String teacherId) {
        ImportPlan plan = new ImportPlan();
        ImportTeacher teacher = new ImportTeacher();
        teacher.setId(teacherId);
        for (int day = 1; day <= 5; day++) {
            for (int slot = 1; slot <= 7; slot++) {
                if (data[day - 1][slot - 1] != 0) {
                    final int dayCopy = day - 1;
                    final int slotCopy = slot - 1;
                    Optional<ImportAssignment> originalAssignment = personalSource.getAssignments().stream()
                            .filter(ass -> ass.getAssignmentNumber() == data[dayCopy][slotCopy]).findFirst();
                    originalAssignment.ifPresent(assignment -> {
                        ImportAssignment newAssignment = new ImportAssignment();
                        newAssignment.setTeacher(teacher);
                        newAssignment.setAssignmentNumber(assignment.getAssignmentNumber());
                        newAssignment.setSubject(assignment.getSubject());
                        newAssignment.setTaughtClass(assignment.getTaughtClass());
                        newAssignment.setRoom(assignment.getRoom());
                        ImportTimeslot newTs = new ImportTimeslot();
                        newTs.setDay(dayCopy + 1);
                        newTs.setSlot(slotCopy + 1);
                        newAssignment.setTimeSlot(newTs);
                        plan.getAssignments().add(newAssignment);
                    });
                }
            }

        }
        return plan;
    }

    private ImportPlan generatePlan(MultiKeyMap<Integer, Boolean> freistunden, String teacherId, int seed) {
        ImportPlan plan = new ImportPlan();
        ImportTeacher teacher = new ImportTeacher();
        teacher.setId(teacherId);
//        teacher.setId("mschmidt");
        int counter = seed;
//        int counter = 0;
        for (int day = 1; day <= 5; day++) {
            for (int slot = 1; slot <= 7; slot++) {
                if (!freistunden.containsKey(day, slot)) {
                    ImportAssignment iteratedOriginalAssignment = plans.get(DEFAULTPLAN).getAssignments().get(counter);
                    ImportAssignment newAssignment = new ImportAssignment();
                    newAssignment.setTeacher(teacher);
                    newAssignment.setAssignmentNumber(counter + 1);
                    newAssignment.setRoom(iteratedOriginalAssignment.getRoom());
                    newAssignment.setSubject(iteratedOriginalAssignment.getSubject());
                    newAssignment.setTaughtClass(iteratedOriginalAssignment.getTaughtClass());
                    ImportTimeslot newTs = new ImportTimeslot();
                    newTs.setDay(day);
                    newTs.setSlot(slot);
                    newAssignment.setTimeSlot(newTs);
                    plan.getAssignments().add(newAssignment);
                    counter++;
                }
            }
        }
        return plan;
    }

    //returns List of Assignments containing each mapped teacherIDs Assignments
    public List<ImportAssignment> getAlgoTeacherPlan(User user, int version) {

        if (userToTeacher.containsKey(user.getUsername()) && version < plans.size()) {

            String teacherID = userToTeacher.get(user.getUsername());
            return plans.get(version).getAssignments().stream().filter(assignment -> assignment.getTeacher().getId().equals(teacherID)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public Optional<ImportAssignment> checkCollision(User user, ImportAssignment source, ImportTimeslot target) {
        ImportPlan targetPlan;
        if (user.getUsername().equals("hschmidt")) {

            targetPlan = meierPlan;
        } else {

            targetPlan = schmidtPlan;
        }

        return targetPlan.getAssignments().stream().filter(assignment -> {
            //TODO:
            return assignment.getTimeSlot().equals(target);
//            return assignment.getTaughtClass().equals(source.getTaughtClass()) &&
//                    assignment.getTimeSlot().equals(target);
        }).findAny();


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

}
