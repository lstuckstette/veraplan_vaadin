package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouterLink;

public class NavigationItemBuilder {

    private String text = "";
    private Class<? extends Component> target = null;


    private String action = "";


    public NavigationTab build() {

        NavigationTab tab = new NavigationTab();

        if (!action.isEmpty()) {
            tab.getElement().setAttribute("onclick", this.action);
        }

        tab.setTarget(target);
        tab.getElement().setText(text);

        return tab;
    }

    public NavigationItemBuilder text(String linkText) {
        this.text = linkText;
        return this;
    }


    public NavigationItemBuilder target(Class<? extends Component> targetClass) {
        this.target = targetClass;
        return this;
    }


    public NavigationItemBuilder action(String action) {
        this.action = action;
        return this;
    }

}
