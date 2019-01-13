package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import org.apache.commons.collections4.map.MultiKeyMap;

@HtmlImport("styles/shared-styles.html")
public class FlowTable extends Div {

    private MultiKeyMap<Integer, FlowTableCell> components;
    private int rows, cols;

    public FlowTable(int cols, int rows) {
        this.rows = rows;
        this.cols = cols;
        components = new MultiKeyMap<>();
        setupContainers();
        setupTableStyle();
        render();
    }

    private void setupContainers() {
        for (int c = 0; c < this.cols; c++)
            for (int r = 0; r < this.rows; r++) {
                FlowTableCell container = new FlowTableCell();
                container.setClassName("table-cell");
                this.components.put(c, r, container);
            }
    }

    private void setupTableStyle() {
        this.setClassName("table-container");

        this.getStyle().set("display", "grid");
        this.getStyle().set("grid-template-columns", "repeat(" + this.cols + ",1fr)");
        this.getStyle().set("grid-template-rows", "repeat(" + this.rows + ",1fr)");

//        remove top & left border from leftmost und top cell/row
        for (int c = 0; c < this.cols; c++) {
            components.get(c, 0).getStyle().set("border-top", "none");

        }

        for (int r = 0; r < this.rows; r++) {
            components.get(0, r).getStyle().set("border-left", "none");
        }
    }

    public void render() {
        this.removeAll();

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                this.add(components.get(c, r));
            }
        }
    }

    public Div getContainer(int col, int rows) {
        return components.get(col - 1, rows - 1);
    }

    public Component getComponent(int col, int row) {
        return this.components.get(col - 1, row - 1).getComponent();
    }

    public void setComponent(int col, int row, Component component) {
        this.components.get(col - 1, row - 1).setComponent(component);
        this.render();
    }

    public void remove(int col, int row) {
        this.components.get(col, row).removeAll();
        this.render();
    }

    public void removeAllComponents() {
        setupContainers();
    }

}
