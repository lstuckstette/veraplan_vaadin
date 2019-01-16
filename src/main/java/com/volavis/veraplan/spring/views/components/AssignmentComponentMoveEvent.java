package com.volavis.veraplan.spring.views.components;

@Deprecated
public class AssignmentComponentMoveEvent {

    private long componentAssignmentId;

//    private int sourceTimeslot, sourceWeekday;
    private int targetTimeslot, targetWeekday;

    public AssignmentComponentMoveEvent(long componentAssignmentId, int targetTimeslot, int targetWeekday) {
        this.componentAssignmentId = componentAssignmentId;
//        this.sourceTimeslot = sourceTimeslot;
//        this.sourceWeekday = sourceWeekday;
        this.targetTimeslot = targetTimeslot;
        this.targetWeekday = targetWeekday;
    }

    public long getComponentAssignmentId() {
        return componentAssignmentId;
    }

//    public int getSourceTimeslot() {
//        return sourceTimeslot;
//    }
//
//    public int getSourceWeekday() {
//        return sourceWeekday;
//    }

    public int getTargetTimeslot() {
        return targetTimeslot;
    }

    public int getTargetWeekday() {
        return targetWeekday;
    }

    @Override
    public String toString() {
        return "ACME: -> [" + targetTimeslot + "," + targetWeekday + "] @" + componentAssignmentId;
    }
}
