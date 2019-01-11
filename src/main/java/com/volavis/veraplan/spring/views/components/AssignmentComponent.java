package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;
import com.volavis.veraplan.spring.persistence.entities.organisation.Usergroup;

public class AssignmentComponent extends VerticalLayout {

    private Assignment assignment;

    private AssignmentContainer parent;

    public AssignmentComponent(Assignment assignment, AssignmentContainer parent) {
        super();
        this.assignment = assignment;
        this.parent = parent;
        this.setMargin(true);
        buildComponent();
    }

    public AssignmentContainer getParentContainer() {
        return parent;
    }

    public void setParentContainer(AssignmentContainer parent) {
        this.parent = parent;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    private void buildComponent() {
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        this.getStyle().set("background", "plum");
        this.setWidth("80%");

        this.add(new Span("Klasse: " + assignment.getUsergroups().get(0).getName()));
        this.add(new Span("Fach: " + assignment.getSubject().getName()));
        this.add(new Span("Raum: " + assignment.getRooms().get(0).getName()));
        this.add(new Span("Lehrer: " + assignment.getLeaders().get(0).getLast_name()));

    }
}
