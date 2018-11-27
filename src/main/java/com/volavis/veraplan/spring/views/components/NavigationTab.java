package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;

import java.util.List;

public class NavigationTab extends Tab {

    private Class<? extends Component> target;

    private List<NavigationTab> submenu;

    public Class<? extends Component> getTarget() {
        return target;
    }

    public void setTarget(Class<? extends Component> target) {
        this.target = target;
    }

    public List<NavigationTab> getSubmenu() {
        return submenu;
    }



    public void setSubmenu(List<NavigationTab> submenu) {
        this.submenu = submenu;
    }
}
