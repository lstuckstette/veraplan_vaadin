package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
public class UsersView extends Div {

    private UserService userService;

    @Autowired
    public UsersView(UserService userService) {
        this.userService = userService;
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        init();
    }

    private void init() {
        var verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(new H2("User Overview:"));
        //verticalLayout.add(new Button("derp"));


        var grid = new Grid<User>();
        grid.setItems(userService.getAllUsers());
        grid.addColumn(User::getFirst_name).setHeader("Firstname").setFlexGrow(0);
        grid.addColumn(User::getLast_name).setHeader("Lastname").setFlexGrow(0);
        grid.addColumn(User::getUsername).setHeader("Username").setFlexGrow(0);
        grid.addColumn(User::getEmail).setHeader("Email").setFlexGrow(1);
        grid.addColumn(User::getPassword).setHeader("Password").setFlexGrow(1);


        verticalLayout.add(grid);

        this.add(verticalLayout);
    }

}
