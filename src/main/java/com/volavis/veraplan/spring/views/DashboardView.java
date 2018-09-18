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
import com.volavis.veraplan.spring.UserSession;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Dashboard")
@Route(value = "home", layout = MainLayout.class)
public class DashboardView extends Div implements BeforeEnterObserver {

    @Autowired
    private UserSession userSession;


    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        if (!userSession.isLoggedIn()) {
            Notification.show("Please login first!");
            //event.rerouteTo(LoginView.class);
        }
    }

    public DashboardView() {
        this.setClassName("w3-container w3-content");
        this.getStyle().set("max-width","1400px");
        this.getStyle().set("margin-top","80px");
        this.getStyle().set("margin-bottom","80px");
        init();
    }

    private void init() {
        add(new Label("This is the home target for this demo"));
        Button button = new Button("Click me",
                event -> Notification.show(userSession.getMessage()));
        add(button);
    }

}
