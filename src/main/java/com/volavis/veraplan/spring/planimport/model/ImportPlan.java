package com.volavis.veraplan.spring.planimport.model;

import java.util.ArrayList;
import java.util.List;

public class ImportPlan {

    private List<ImportAssignment> assignments = new ArrayList<>();
    private int millisSpentBySolver;
    private int hardScore;
    private int numDays;
    private int numSlots;

    public ImportPlan() {
    }

    public List<ImportAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<ImportAssignment> assignments) {
        this.assignments = assignments;
    }

    public int getMillisSpentBySolver() {
        return millisSpentBySolver;
    }

    public void setMillisSpentBySolver(int millisSpentBySolver) {
        this.millisSpentBySolver = millisSpentBySolver;
    }

    public int getHardScore() {
        return hardScore;
    }

    public void setHardScore(int hardScore) {
        this.hardScore = hardScore;
    }

    public int getNumDays() {
        return numDays;
    }

    public void setNumDays(int numDays) {
        this.numDays = numDays;
    }

    public int getNumSlots() {
        return numSlots;
    }

    public void setNumSlots(int numSlots) {
        this.numSlots = numSlots;
    }
}
