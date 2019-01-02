package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

@Deprecated
public class AssignmentComponent extends VerticalLayout {

    private Assignment assignment;
    private int dayOfWeek = -1;

    public AssignmentComponent(Assignment assignment) {
        super();
        this.assignment = assignment;
        this.buildComponent();
    }

    public AssignmentComponent(Assignment assignment, int dayOfWeek) {
        super();
        this.dayOfWeek = dayOfWeek;
        this.assignment = assignment;
        this.buildComponent();
    }

    private void buildComponent() {
        if (dayOfWeek == this.getDayOfWeek()) {
            this.add(new Span("yay"));
        } else {
            this.add(new Span("nay"));
        }
    }

    public int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();

        if (!this.assignment.getTimeSlots().isEmpty() && this.assignment.getTimeSlots().get(0).getDate() != null) {
            calendar.setTime(this.assignment.getTimeSlots().get(0).getDate());
            return calendar.get(Calendar.DAY_OF_WEEK);
        } else {
            return 0;
        }
    }

    public List<Integer> getTimeslotIndices() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (TimeSlot t : assignment.getTimeSlots()) {
            indices.add(t.getEnumerator());
        }
        //sort
        indices.sort(Comparator.naturalOrder());
        return indices;
    }

}
