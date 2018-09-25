package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.views.RegisterView;


@Tag("navigation-usermenu-registerlogin")
@HtmlImport("components/navigation-usermenu-registerlogin.html")
public class NavigationBarUsermenuRegisterlogin extends PolymerTemplate<TemplateModel> {


    private RouterLink register;

    public NavigationBarUsermenuRegisterlogin(){

    }
}
