package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouterLink;

import java.util.List;

public class NavigationItemBuilder {

    private String text = "";
    private Class<? extends Component> targetClass = null;

    private boolean onClickSet = false;
    private String onClick = "";


    public Tab build() {

        Tab tab = new Tab();

        if (this.onClickSet) {
            tab.getElement().setAttribute("on-click", this.onClick);
        }
        if (targetClass != null) {
            tab.add(new RouterLink(this.text, this.targetClass));
        } else {
            tab.getElement().setText(this.text);
        }
        return tab;
    }

    public NavigationItemBuilder linkText(String linkText) {
        this.text = linkText;
        return this;
    }


    public NavigationItemBuilder targetClass(Class<? extends Component> targetClass) {
        this.targetClass = targetClass;
        return this;
    }


    public NavigationItemBuilder onClick(String onClick) {
        this.onClick = onClick;
        this.onClickSet = true;
        return this;
    }

}
