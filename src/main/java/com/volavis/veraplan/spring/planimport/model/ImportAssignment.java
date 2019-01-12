package com.volavis.veraplan.spring.planimport.model;

public class ImportAssignment {

    private ImportRoom room;
    private ImportTeacher teacher;
    private ImportTimeslot timeSlot;
    private ImportClass taughtClass;
    private ImportSubject subject;
    private int assignmentNumber;

    public ImportAssignment() {
    }

    public ImportRoom getRoom() {
        return room;
    }

    public void setRoom(ImportRoom room) {
        this.room = room;
    }

    public ImportTeacher getTeacher() {
        return teacher;
    }

    public void setTeacher(ImportTeacher teacher) {
        this.teacher = teacher;
    }

    public ImportTimeslot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(ImportTimeslot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public ImportClass getTaughtClass() {
        return taughtClass;
    }

    public void setTaughtClass(ImportClass taughtClass) {
        this.taughtClass = taughtClass;
    }

    public ImportSubject getSubject() {
        return subject;
    }

    public void setSubject(ImportSubject subject) {
        this.subject = subject;
    }

    public int getAssignmentNumber() {
        return assignmentNumber;
    }

    public void setAssignmentNumber(int assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }
}
