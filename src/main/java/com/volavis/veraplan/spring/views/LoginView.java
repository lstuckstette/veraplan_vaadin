package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("login-view")
@HtmlImport("src/login-view.html")
@Route(value = "login")
//@StyleSheet("https://www.w3schools.com/w3css/4/w3.css")
//@StyleSheet("https://www.w3schools.com/lib/w3-theme-blue-grey.css")
public class LoginView extends Component implements HasUrlParameter<String> {

    public LoginView() {
        //init();
        //this.setClassName("w3-container");
    }

    private void initModal(){
        H2 headline = new H2("Veraplan-Test-Landing");

    }

    private void init() {
        //add(new Label("derp!"));

        HtmlContainer form = new HtmlContainer("form");
        form.setClassName("w3-display-middle w3-card-4");
        form.getElement().setAttribute("method", "post");
        form.getElement().setAttribute("action", "login");

        Paragraph usernameP = new Paragraph();
        Input usernameI = new Input();
        usernameI.setClassName("w3-input");
        usernameI.setType("text");
        usernameI.getElement().setAttribute("required", "");
        usernameI.getElement().setAttribute("name", "username");
        usernameI.getStyle().set("width", "90%");
        Label usernameL = new Label("Username/Email");
        usernameL.setClassName("w3-label w3-validate");
        usernameP.add(usernameI, usernameL);

        Paragraph passwordP = new Paragraph();
        Input passwordI = new Input();
        passwordI.setClassName("w3-input");
        passwordI.setType("password");
        passwordI.getElement().setAttribute("required", "");
        passwordI.getElement().setAttribute("name", "password");
        passwordI.getStyle().set("width", "90%");
        Label passwordL = new Label("Password");
        passwordL.setClassName("w3-label w3-validate");
        usernameP.add(passwordI, passwordL);


        Paragraph loginButtonP = new Paragraph();
        HtmlContainer loginButton = new HtmlContainer("button");
        loginButton.setClassName("w3-button w3-selection w3-ripple");
        loginButton.getElement().setAttribute("type", "submit");
        loginButton.setText("Login");
        loginButtonP.add(loginButton);

        Paragraph registerButtonP = new Paragraph();
        Button registerButton = new Button("Register", event -> handleRegister(event));
        registerButton.setClassName("w3-button w3-selection w3-ripple");
        registerButtonP.add(registerButton);

        form.add(usernameP, passwordP, loginButtonP, registerButtonP);
        //this.add(form);
    }

    private void handleRegister(ClickEvent<Button> event) {
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }
}
