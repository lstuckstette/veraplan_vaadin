package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.volavis.veraplan.spring.persistence.entities.ressources.Assignment;

@Deprecated
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

        String klasse = assignment.getUsergroups().isEmpty() ? "??" : assignment.getUsergroups().get(0).getName();
        String subject = assignment.getSubject() == null ? "??" : assignment.getSubject().toString();
        String room = assignment.getRooms().isEmpty() ? "??" : assignment.getRooms().get(0).getName();
        String leader = assignment.getLeaders().isEmpty() ? "??" : assignment.getLeaders().get(0).getLast_name();

        this.add(new Span("Klasse: " + klasse));
        this.add(new Span("Fach: " + subject));
        this.add(new Span("Raum: " + room));
        this.add(new Span("Lehrer: " + leader));

    }
}
