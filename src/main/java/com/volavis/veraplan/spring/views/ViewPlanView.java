package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.views.components.CollaborationToolkit;
import com.volavis.veraplan.spring.views.components.Weekday;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Plan")
@Route(value = "plan", layout = MainLayout.class)
public class ViewPlanView extends Div {


    @Autowired
    public ViewPlanView(UserService userService) {

        CollaborationToolkit toolkit = new CollaborationToolkit(userService, "1337");

        VerticalLayout gloablLayout = new VerticalLayout();

        initView(gloablLayout);


        toolkit.add(gloablLayout);
        this.add(toolkit);
    }

    private void initView(VerticalLayout globalLayout) {

        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Map<Weekday, List<Assignment>> planModel = new HashMap<>();

        Grid<Weekday> planGrid = new Grid<>();


        planGrid.setColumns();

    }
}
