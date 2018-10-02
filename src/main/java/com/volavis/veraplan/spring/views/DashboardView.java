package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.MainLayout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@PageTitle("Dashboard")
@Tag("dashboard-view")
@HtmlImport("views/dashboard-view.html")
@Route(value = "", layout = MainLayout.class)
public class DashboardView extends PolymerTemplate<TemplateModel> {

    public DashboardView() {
         init();
    }

    private void init() {
        Div content = new Div();
        content.setClassName("w3-container w3-content");
        content.add(new Label("This is the home target for this demo"));
        Button button = new Button("Click me",
                event -> Notification.show("derp"));
        content.add(button);
        this.getElement().appendChild(content.getElement());
    }

}
