package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;

@PageTitle("Register")
@Route(value="register", layout= MainLayout.class)
public class RegisterView extends Div {

    public RegisterView(){
        H2 headline = new H2("Register");
    }
}
