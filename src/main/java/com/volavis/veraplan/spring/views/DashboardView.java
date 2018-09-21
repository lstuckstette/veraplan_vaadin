package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;

import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
public class DashboardView extends Div implements BeforeEnterObserver {




    @Override
    public void beforeEnter(BeforeEnterEvent event) {


    }

    public DashboardView() {
         init();
    }

    private void init() {
        add(new Label("This is the home target for this demo"));
        Button button = new Button("Click me",
                event -> Notification.show("derp"));
        add(button);
    }

}
