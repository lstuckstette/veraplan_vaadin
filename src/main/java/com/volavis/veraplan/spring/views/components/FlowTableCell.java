package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class FlowTableCell extends Div {

    private Component component;

    public FlowTableCell() {
        super();
//        this.setAlignItems(Alignment.CENTER);
//        this.setJustifyContentMode(JustifyContentMode.CENTER);
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.removeAll();
        this.component = component;
        this.add(component);
    }
}
