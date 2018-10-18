package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
public class UsersView extends Div {

    private UserService userService;

    @Autowired
    public UsersView(UserService userService){
        this.userService = userService;
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        init();
    }

    private void init(){
        add(new H2("User Overview:"));
        Grid<User> grid = new Grid<>();
        grid.setItems(userService.getAllUsers());
        grid.addColumn(User::getFirst_name).setHeader("Firstname");
        grid.addColumn(User::getLast_name).setHeader("Lastname");
        grid.addColumn(User::getUsername).setHeader("Username");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getPassword).setHeader("Password");
        add(grid);
    }

}
