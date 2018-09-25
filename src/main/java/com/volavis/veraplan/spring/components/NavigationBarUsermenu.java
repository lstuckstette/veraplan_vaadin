package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.security.SecurityUtils;

@Tag("navigation-usermenu")
@HtmlImport("components/navigation-usermenu.html")
public class NavigationBarUsermenu extends PolymerTemplate<TemplateModel> {


    public NavigationBarUsermenu() {
        if (SecurityUtils.isUserLoggedIn()) {
            this.getElement().appendChild(new NavigationBarUsermenuLoggedin().getElement());
        } else {
            this.getElement().appendChild(new NavigationBarUsermenuRegisterlogin().getElement());
        }

    }



}
