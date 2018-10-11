package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;

public class NavigationTab extends Tab {

    private Class<? extends Component> target;

    public Class<? extends Component> getTarget() {
        return target;
    }

    public void setTarget(Class<? extends Component> target) {
        this.target = target;
    }

}
