package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;

@Route(value = "login")
@StyleSheet("https://www.w3schools.com/w3css/4/w3.css")
@StyleSheet("https://www.w3schools.com/lib/w3-theme-blue-grey.css")
public class LoginView extends Div {

    public LoginView() {
        init();
        this.setClassName("w3-container");
    }

    private void init() {
        //add(new Label("derp!"));

        HtmlContainer form = new HtmlContainer("form");
        form.setClassName("w3-display-middle w3-card-4");

        Paragraph usernameP = new Paragraph();
        Input usernameI = new Input();
        usernameI.setClassName("w3-input");
        usernameI.setType("text");
        usernameI.getElement().setAttribute("required", "");
        usernameI.getStyle().set("width", "90%");
        Label usernameL = new Label("Username/Email");
        usernameL.setClassName("w3-label w3-validate");
        usernameP.add(usernameI, usernameL);

        Paragraph passwordP = new Paragraph();
        Input passwordI = new Input();
        passwordI.setClassName("w3-input");
        passwordI.setType("password");
        passwordI.getElement().setAttribute("required", "");
        passwordI.getStyle().set("width", "90%");
        Label passwordL = new Label("Password");
        passwordL.setClassName("w3-label w3-validate");
        usernameP.add(passwordI, passwordL);


        Paragraph loginButtonP = new Paragraph();
        HtmlContainer loginButton = new HtmlContainer("button");
        loginButton.setClassName("w3-button w3-selection w3-ripple");
        loginButton.setText("Login");
        loginButtonP.add(loginButton);

        Paragraph registerButtonP = new Paragraph();
        Button registerButton = new Button("Register", event -> handleRegister(event));
        registerButton.setClassName("w3-button w3-selection w3-ripple");
        registerButtonP.add(registerButton);

        form.add(usernameP, passwordP, loginButtonP, registerButtonP);
        this.add(form);
    }

    private void handleRegister(ClickEvent<Button> event) {
    }

}
