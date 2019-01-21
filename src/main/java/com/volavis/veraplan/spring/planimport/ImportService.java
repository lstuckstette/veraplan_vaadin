package com.volavis.veraplan.spring.planimport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.ressources.Assignment;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.planimport.model.*;
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

    private List<ImportAssignment> schmidtRating1;
    private List<ImportAssignment> schmidtRating2;
    private List<ImportAssignment> schmidtRating3;

    private List<ImportAssignment> meierRating1;
    private List<ImportAssignment> meierRating2;
    private List<ImportAssignment> meierRating3;


    //personal teacher matrix[day][slots]
    private int[][] schmidtAssignmentMatrix =
            {{1, 3, 70, 7, 52, 58, 0}, {2, 4, 50, 8, 53, 84, 0}, {0, 12, 0, 0, 55, 85, 0}, {10, 0, 26, 31, 56, 86, 0}, {11, 14, 27, 0, 0, 0, 75}};
    private int[][] meierAssignmentMatrix =
            {{0, 43, 28, 40, 79, 88, 0}, {15, 20, 29, 41, 80, 37, 0}, {17, 0, 0, 54, 0, 0, 0}, {18, 22, 47, 42, 82, 34, 0}, {19, 23, 0, 51, 83, 36, 0}};

    private int[][] schmidtFinalAssignmentMatrix =
            {{0, 3, 70, 7, 52, 58, 0}, {2, 4, 50, 8, 53, 84, 0}, {1, 12, 0, 75, 55, 85, 0}, {10, 0, 26, 31, 56, 86, 0}, {11, 14, 27, 0, 0, 0, 0}};
    private int[][] meierFinalAssignmentMatrix =
            {{17, 43, 28, 40, 79, 88, 0}, {15, 20, 29, 41, 80, 37, 0}, {0, 0, 0, 0, 0, 0, 0}, {18, 22, 47, 42, 82, 34, 0}, {19, 23, 0, 51, 83, 36, 75}};

    @PostConstruct
    public void setup() {


        //Read generated plans through json files:
//        for (int i = 1; i <= 3; i++) {
//            ImportPlan iPlan = null;
//            try {
//                Path jsonPath = ResourceUtils.getFile("classpath:database_dummydata/generated-plan-" + i + ".json").toPath();
//                BufferedReader reader = Files.newBufferedReader(jsonPath);
//                Type type = new TypeToken<ImportPlan>() {
//                }.getType();
//                Gson gson = new Gson();
//                iPlan = gson.fromJson(reader, type);
////                logger.info("READ " + iPlan.getAssignments().size() + " Mock-Assignments!");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            this.plans.add(iPlan);
//        }

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

        //Custom plans:
        generatePersonalPlans();

        schmidtRating1 = generateRatePlan(schmidtPlan);
        schmidtRating2 = generateRatePlan(schmidtPlan);
        schmidtRating3 = generateRatePlan(schmidtPlan);

        meierRating1 = generateRatePlan(meierPlan);
        meierRating2 = generateRatePlan(meierPlan);
        meierRating3 = generateRatePlan(meierPlan);

    }

    private void generatePersonalPlans() {

        //set same class for each 'collision' pair...
        ImportClass equalClass1 = new ImportClass();
        equalClass1.setId("Klasse 2a");
        ImportClass equalClass2 = new ImportClass();
        equalClass2.setId("Klasse 4a");
        personalSource.getAssignments().stream()
                .filter(assignment -> assignment.getAssignmentNumber() == 1 || assignment.getAssignmentNumber() == 17)
                .findFirst()
                .ifPresent(assignment -> assignment.setTaughtClass(equalClass1));

        personalSource.getAssignments().stream()
                .filter(assignment -> assignment.getAssignmentNumber() == 54 || assignment.getAssignmentNumber() == 75)
                .findFirst()
                .ifPresent(assignment -> assignment.setTaughtClass(equalClass2));


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

//    private ImportPlan generatePlan(MultiKeyMap<Integer, Boolean> freistunden, String teacherId, int seed) {
//        ImportPlan plan = new ImportPlan();
//        ImportTeacher teacher = new ImportTeacher();
//        teacher.setId(teacherId);
////        teacher.setId("mschmidt");
//        int counter = seed;
////        int counter = 0;
//        for (int day = 1; day <= 5; day++) {
//            for (int slot = 1; slot <= 7; slot++) {
//                if (!freistunden.containsKey(day, slot)) {
//                    ImportAssignment iteratedOriginalAssignment = plans.get(DEFAULTPLAN).getAssignments().get(counter);
//                    ImportAssignment newAssignment = new ImportAssignment();
//                    newAssignment.setTeacher(teacher);
//                    newAssignment.setAssignmentNumber(counter + 1);
//                    newAssignment.setRoom(iteratedOriginalAssignment.getRoom());
//                    newAssignment.setSubject(iteratedOriginalAssignment.getSubject());
//                    newAssignment.setTaughtClass(iteratedOriginalAssignment.getTaughtClass());
//                    ImportTimeslot newTs = new ImportTimeslot();
//                    newTs.setDay(day);
//                    newTs.setSlot(slot);
//                    newAssignment.setTimeSlot(newTs);
//                    plan.getAssignments().add(newAssignment);
//                    counter++;
//                }
//            }
//        }
//        return plan;
//    }

    //returns List of Assignments containing each mapped teacherIDs Assignments
    public List<ImportAssignment> getRatingPlan(User user, int version) {

        if (user.getUsername().equals("hschmidt")) {
            switch (version + 1) {
                case 1:
                    return schmidtRating1;
                case 2:
                    return schmidtRating2;
                case 3:
                    return schmidtRating3;
            }

        } else {
            switch (version + 1) {
                case 1:
                    return meierRating1;
                case 2:
                    return meierRating2;
                case 3:
                    return meierRating3;
            }
        }


//        if (userToTeacher.containsKey(user.getUsername()) && version < plans.size()) {
//
//            String teacherID = userToTeacher.get(user.getUsername());
//            return plans.get(version).getAssignments().stream().filter(assignment -> assignment.getTeacher().getId().equals(teacherID)).collect(Collectors.toList());
//        }
        return new ArrayList<>();
    }

    private List<ImportAssignment> generateRatePlan(ImportPlan source) {
        List<ImportAssignment> assignments = new ArrayList<>();


        //create Stack holding all possible timeslots:
        Stack<ImportTimeslot> timeslots = new Stack<>();
        for (int day = 1; day <= 5; day++) {
            for (int slot = 1; slot <= 6; slot++) {
                ImportTimeslot ts = new ImportTimeslot();
                ts.setSlot(slot);
                ts.setDay(day);
                timeslots.push(ts);
            }
        }
        //randomize timeslots in stack
        Collections.shuffle(timeslots);

        //generate new assignment and fill with values from original plan - set random timeslot!
        for (int count = 1; count <= 20; count++) {
            ImportAssignment newAssignment = new ImportAssignment();
            newAssignment.setAssignmentNumber(source.getAssignments().get(count).getAssignmentNumber());
            newAssignment.setTaughtClass(source.getAssignments().get(count).getTaughtClass());
            newAssignment.setRoom(source.getAssignments().get(count).getRoom());
            newAssignment.setTeacher(source.getAssignments().get(count).getTeacher());
            newAssignment.setSubject(source.getAssignments().get(count).getSubject());
            newAssignment.setTimeSlot(timeslots.pop());
            assignments.add(newAssignment);
        }

        return assignments;
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

//    public Optional<ImportAssignment> checkCollision(int planIndex, ImportAssignment source, ImportTimeslot target) {
//
//        if (planIndex >= plans.size()) {
//            return Optional.empty();
//        }
//
//        return this.plans.get(planIndex).getAssignments().stream().filter(assignment -> {
//            //TODO:
//            return assignment.getTaughtClass().equals(source.getTaughtClass()) &&
//                    assignment.getTimeSlot().equals(target);
//        }).findAny();
//
//    }

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
