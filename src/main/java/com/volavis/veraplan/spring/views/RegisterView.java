package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.components.NavigationBar;

@PageTitle("Register")
@Tag("register-view")
@HtmlImport("views/register-view.html")
@Route(value="register")
public class RegisterView extends Div {

    @Id("main-nav")
    private NavigationBar navbar;

    public RegisterView(){

        //TODO: populate Navbar

        H2 headline = new H2("Register");
        this.getElement().appendChild(headline.getElement());
        //TODO: implement this in Template
    }
}
