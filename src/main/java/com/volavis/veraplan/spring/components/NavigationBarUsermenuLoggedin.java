package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.security.SecurityUtils;

@Tag("navigation-usermenu-loggedin")
@HtmlImport("components/navigation-usermenu-loggedin.html")
public class NavigationBarUsermenuLoggedin extends PolymerTemplate<NavigationBarUserMenuLoggedinModel> {



    public NavigationBarUsermenuLoggedin() {

        getModel().setUsername(SecurityUtils.getUsername());
    }


}
