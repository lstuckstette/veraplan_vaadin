package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class AssContainer extends VerticalLayout {
    private List<AssComponent> assignmentComponents = new ArrayList<>();
    private int row, col;

    public AssContainer(int col, int row) {
        this.col = col;
        this.row = row;
        this.setAlignItems(Alignment.CENTER);
        this.setHeight("100%");
        render();
    }

    public void addAssignmentComponent(AssComponent assignment) {
        this.assignmentComponents.add(assignment);
        render();
    }

    public void removeAssignment(AssComponent assignment) {
        this.assignmentComponents.remove(assignment);
        render();
    }

    public List<AssComponent> getAssComponents() {
        return this.assignmentComponents;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void render() {
        this.removeAll();
        assignmentComponents.forEach(this::add);
    }

}