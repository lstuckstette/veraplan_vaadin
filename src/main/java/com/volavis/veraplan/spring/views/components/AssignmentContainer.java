package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;

import java.util.ArrayList;
import java.util.Optional;

public class AssignmentContainer extends Div {

    private int timeslotEnumerator;
    private int weekday;

    //    private ArrayList<Assignment> assignments = new ArrayList<>();
    private ArrayList<AssignmentComponent> assignmentComponents = new ArrayList<>();

    public AssignmentContainer(int timeslotEnumerator, int weekday) {

        this.timeslotEnumerator = timeslotEnumerator;
        this.weekday = weekday;
        this.render();
    }

    public int getSpan() {
        return assignmentComponents.size();
    }

    public int getTimeslotEnumerator() {
        return timeslotEnumerator;
    }

    public void setTimeslotEnumerator(int timeslotEnumerator) {
        this.timeslotEnumerator = timeslotEnumerator;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public void addAssignmentComponent(AssignmentComponent assignmentComponent) {
        assignmentComponents.add(assignmentComponent);
        this.render();
    }

    public void removeAssignmentComponent(AssignmentComponent assignmentComponent) {
        assignmentComponents.remove(assignmentComponent);
        this.render();
    }

    public boolean containsAssignmentComponent(AssignmentComponent assignmentComponent) {
        return assignmentComponents.contains(assignmentComponent);
    }

    public Optional<AssignmentComponent> getAssignmentComponentFromAssignmentId(long id) {
        for (AssignmentComponent ac : assignmentComponents) {
            if (ac.getAssignment().getId() == id) {
                return Optional.of(ac);
            }
        }
        return Optional.empty();
    }

    public void render() {
        //clear current container content
        this.removeAll();

        //'re'-render this component based on count of assignments assigned:

        if (assignmentComponents.isEmpty()) { //CASE empty
            this.getStyle().remove("background");
//            this.getStyle().set("background", "silver");
            this.getStyle().set("border", "1px dotted");
//            this.add(new Span("e"));

        } else if (assignmentComponents.size() > 1) { //CASE collision
            this.getStyle().remove("border");
//            this.getStyle().set("background", "repeating-linear-gradient(-45deg,#fff,#fff 5px,#f00 5px,#f00 10px)");
            this.getStyle().set("background", "red");
            assignmentComponents.forEach(this::add);

        } else { //CASE 'normal' single assignment
            this.getStyle().remove("background");
            this.getStyle().remove("border");
            assignmentComponents.forEach(this::add);
        }

    }
}
