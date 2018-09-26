package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.components.NavigationBar;
import com.volavis.veraplan.spring.components.NavigationItemBuilder;

@PageTitle("Register")
@Tag("register-view")
@HtmlImport("views/register-view.html")
@Route(value="register")
public class RegisterView extends PolymerTemplate<TemplateModel> {

    @Id("main-nav")
    private NavigationBar navbar;

    public RegisterView(){

        //TODO: populate Navbar
        navbar.addNavItem(new NavigationItemBuilder().linkText("Home").href("#").backgroundColor("w3-white").build());

        H2 headline = new H2("Register");
        this.getElement().appendChild(headline.getElement());
        //TODO: implement this in Template
    }
}
