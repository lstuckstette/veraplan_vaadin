package com.volavis.veraplan.spring.views;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.components.NavigationBar;
import com.volavis.veraplan.spring.components.NavigationItemBuilder;


@Tag("login-view")
@HtmlImport("views/login-view.html") //local path: src/main/webapp/frontend ~
@Route(value = "login")
public class LoginView extends PolymerTemplate<TemplateModel> implements HasUrlParameter<String> {

    private static final Logger logger = LoggerFactory.getLogger(LoginView.class);

    @Id("main-nav")
    NavigationBar navbar;

    public LoginView() {

        //for icons inside shadow-dom
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");

        navbar.addNavItem(new NavigationItemBuilder().linkText("Home").href("#").backgroundColor("w3-white").build());
        //navbar.addNavItem(new NavigationItemBuilder().linkText("About").href("#about").build());
        //navbar.addNavItem(new NavigationItemBuilder().linkText("Register").onClick("handleRegister").build());
        //navbar.addNavItem(new NavigationItemBuilder().linkText("Login").rightAlign(true).onClick("openLoginModal").build());

    }

    private void initOLD() {
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
        Button registerButton = new Button("Register");
        registerButton.setClassName("w3-button w3-selection w3-ripple");
        registerButtonP.add(registerButton);

        form.add(usernameP, passwordP, loginButtonP, registerButtonP);
        //this.add(form);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }
}
