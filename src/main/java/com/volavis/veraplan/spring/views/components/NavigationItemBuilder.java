package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationItemBuilder {

    private String text = "";
    private Class<? extends Component> target = null;
    private List<NavigationTab> submenu = new ArrayList<>();


    private String action = "";


    public NavigationTab build() {

        NavigationTab tab = new NavigationTab();

        if (!action.isEmpty()) {
            tab.getElement().setAttribute("onclick", this.action);
        }

        tab.setTarget(target);
        tab.getElement().setText(text);
        tab.setSubmenu(submenu);

        return tab;
    }

    public NavigationItemBuilder text(String linkText) {
        this.text = linkText;
        return this;
    }

    public NavigationItemBuilder submenu(NavigationTab... tabs) {
        submenu.addAll(Arrays.asList(tabs));
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
