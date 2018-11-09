package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;


import org.springframework.security.access.annotation.Secured;

@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
public class DashboardView extends Div {


    public DashboardView() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        init();
    }

    private void init() {


        add(new Label("This is the home target for this demo"));
        Button button = new Button("Click me",
                event -> Notification.show("Notification test~"));
        add(button);

    }

}
