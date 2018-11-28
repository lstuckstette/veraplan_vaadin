package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.views.components.CollaborationToolkit;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Plan")
@Route(value = "plan", layout = MainLayout.class)
public class PlanView extends Div {


    private CollaborationToolkit toolkit;

    @Autowired
    public PlanView(UserService userService) {
        this.toolkit = new CollaborationToolkit(userService);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        verticalLayout.add(new H2("Plan"));

        for (int i = 0; i < 20; i++) {
            verticalLayout.add(new Span("derpy!"));
        }


        toolkit.add(verticalLayout);
        this.add(toolkit);
    }
}
