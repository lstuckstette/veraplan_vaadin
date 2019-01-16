package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.volavis.veraplan.spring.planimport.model.ImportAssignment;

public class AssComponent extends VerticalLayout {

    private ImportAssignment assignment;
    private AssContainer parentContainer;


    public AssComponent(ImportAssignment assignment) {
        this.assignment = assignment;
        this.setAlignItems(Alignment.CENTER);
        this.addClassName("assignment-component");
        render();
    }

    public AssComponent(AssContainer parentContainer) {
        this.parentContainer = parentContainer;
        this.setAlignItems(Alignment.CENTER);
        this.addClassName("assignment-component");
    }

    public AssContainer getParentContainer() {
        return parentContainer;
    }

    public void setOwn() {
        this.removeClassName("foreign");
        this.addClassName("own");
    }

    public void setForeign() {
        this.removeClassName("own");
        this.addClassName("foreign");
    }

    public void setParentContainer(AssContainer parentContainer) {
        this.parentContainer = parentContainer;
    }

    public ImportAssignment getAssignment() {
        return this.assignment;
    }

    public void setAssignment(ImportAssignment assignment) {
        this.assignment = assignment;
        render();
    }

    public void render() {
        this.removeAll();
        if (assignment != null) {
            this.add(new Span(assignment.getTaughtClass().getId()));
            this.add(new Span("F: " + assignment.getSubject().getId()));
            this.add(new Span("R: " + assignment.getRoom().getId()));
        }
    }


}