package com.volavis.veraplan.spring.planimport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import com.volavis.veraplan.spring.persistence.entities.ressources.Assignment;
import com.volavis.veraplan.spring.planimport.model.ImportPlan;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ImportService {

    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);

    public void readPlan() {

        try {
            Path jsonPath = ResourceUtils.getFile("classpath:database_dummydata/generated-plan.json").toPath();
            BufferedReader reader = Files.newBufferedReader(jsonPath);
            Type type = new TypeToken<ImportPlan>() {
            }.getType();
            Gson gson = new Gson();
            ImportPlan iPlan = gson.fromJson(reader, type);
            logger.info("READ " + iPlan.getAssignments().size() + " Mock-Assignments!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Assignment> getMockTeacherPlan(ImportPlan plan, String teacherID) {
        //TODO
        return null;
    }

}
