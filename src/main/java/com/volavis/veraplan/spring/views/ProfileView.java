package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.Role;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)

public class ProfileView extends Div {

    private UserService userService;


    @Autowired
    public ProfileView(UserService userService) {
        this.userService = userService;

        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");

        init();

    }

    public void init() {

        User currentUser = userService.getByUsernameOrEmail(SecurityUtils.getUsername());

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        H1 headline = new H1("Profile:");
        verticalLayout.add(headline);

        FormLayout formlayout = new FormLayout();

        TextField firstname = new TextField();
        firstname.setLabel("Firstname");
        firstname.setValue(currentUser.getFirst_name());
        firstname.setEnabled(false);
        TextField lastname = new TextField();
        lastname.setLabel("Lastname");
        lastname.setValue(currentUser.getLast_name());
        lastname.setEnabled(false);
        TextField username = new TextField();
        username.setLabel("Username");
        username.setValue(currentUser.getUsername());
        username.setEnabled(false);
        TextField email = new TextField();
        email.setLabel("Email");
        email.setValue(currentUser.getEmail());
        email.setEnabled(false);

        Grid<Role> roles = new Grid<>();
        roles.setItems(currentUser.getRoles());
        roles.addColumn(Role::getName).setHeader("Roles");

        formlayout.add(firstname, lastname, username, email, roles);

        verticalLayout.add(formlayout);

        this.add(verticalLayout);
    }
}
