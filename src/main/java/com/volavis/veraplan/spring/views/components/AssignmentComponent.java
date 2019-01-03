package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;

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
        //TODO: finish this
        this.getStyle().set("background", "plum");
        this.setWidth("80%");
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        this.add(new Span(assignment.getName()));
    }
}
