package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.service.RoleService;
import com.volavis.veraplan.spring.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Veraplan - Manage Users")
@Route(value = "administration/users", layout = MainLayout.class)
public class ManageUsersView extends Div {

    private UserService userService;

    //TODO: port this to manageUsersView & delete this

    @Autowired
    public ManageUsersView(UserService userService) {
        this.userService = userService;

        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        init();
    }

    private void init() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(new H1("Manage Users"));



        Grid<User> grid = new Grid<>();
        grid.setItems(userService.getAllUsers());
        grid.addColumn(User::getFirst_name).setHeader("Firstname").setFlexGrow(0);
        grid.addColumn(User::getLast_name).setHeader("Lastname").setFlexGrow(0);
        grid.addColumn(User::getUsername).setHeader("Username").setFlexGrow(0);
        grid.addColumn(User::getEmail).setHeader("Email").setFlexGrow(1);
        grid.addColumn(User::getPassword).setHeader("Password").setFlexGrow(1);
        grid.addComponentColumn(user -> {
            ComboBox<String> roleBox = new ComboBox<>();
            roleBox.setItems(user.getRoles().stream().map(role -> role.getName().toString()));
            return roleBox;
        }).setHeader("Roles");


        verticalLayout.add(grid);

        this.add(verticalLayout);
    }

}
