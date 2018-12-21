package com.volavis.veraplan.spring.views.views_coredata;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.views.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@PageTitle("Veraplan - Enter Rooms")
@Route(value = "coredata/room", layout = MainLayout.class)
public class EnterRoomsView extends Div {

    private final UserService service;

    @Autowired
    public EnterRoomsView(UserService service) {
        this.service = service;
        initView();
    }

    private void initView() {

        EntityManagerComponentBuilder<User, UserField> emcb = new EntityManagerComponentBuilder<>(service, UserField.class)
                .setHeadline("H")
                .setSubHeadline("SH")
                .addGridColumn(User::getId, "Id")
                .addGridColumn(User::getFirst_name, "Firstname")
                .addGridColumn(User::getLast_name, "Lastname")
                .addGridColumn(User::getUsername, "Username")
                .addGridColumn(User::getEmail, "Email")
                .addGridComponentColumn(user -> {
                    Div container = new Div();
                    user.getRoles().forEach(role -> container.add(new Span(role.getName().toString() + ";")));
                    return container;
                }, "Roles");

        emcb = emcb.setEditEntityRenderer(this::editEntityComponent)
                .setAddEntityComponent(this.addEntityComponent());

        this.add(emcb.buildComponent());

//        EntityManagerComponent<User, UserField> manageRooms = new EntityManagerComponent<>(service, UserField.class, "Manage Users <Generic>",
//                "Browse all Users or filter the List. Clicking on an item will show an editor window.");
//
//        manageRooms.addGridColumn(User::getId, "Id");
//        manageRooms.addGridColumn(User::getFirst_name, "Firstname");
//        manageRooms.addGridColumn(User::getLast_name, "Lastname");
//        manageRooms.addGridColumn(User::getUsername, "Username");
//        manageRooms.addGridColumn(User::getEmail, "Email");
//        manageRooms.addGridComponentColumn(user -> {
//            Div container = new Div();
//            user.getRoles().forEach(role -> container.add(new Span(role.getName().toString() + ";")));
//            return container;
//        }, "Roles");
//
//
//        manageRooms.addEditEntityTextField("Firstname", User::getFirst_name, User::setFirst_name);
//        manageRooms.addEditEntityTextField("Lastname", User::getLast_name, User::setLast_name);
//        manageRooms.addEditEntityTextField("Username", User::getUsername, User::setUsername);
//        manageRooms.addEditEntityTextField("Email", User::getEmail, User::setEmail);
//        manageRooms.addEditEntityPasswordField("Password", User::getPassword,
//                (usr, pass) -> usr.setPassword(new BCryptPasswordEncoder().encode(pass)));
//        manageRooms.addEditEntityCollection("Roles",User::getRoles,);
//
//
//        this.add(manageRooms);
    }

    private VerticalLayout editEntityComponent(User user) {
        VerticalLayout editEntityLayout = new VerticalLayout();
        editEntityLayout.add(new Span("EDIT"));
        return editEntityLayout;
    }

    private VerticalLayout addEntityComponent() {
        VerticalLayout addEntityLayout = new VerticalLayout();
        addEntityLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        addEntityLayout.add(new Span("ADD"));
        return addEntityLayout;
    }

}
