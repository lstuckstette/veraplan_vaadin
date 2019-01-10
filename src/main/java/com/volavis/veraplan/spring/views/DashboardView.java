package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;


import org.springframework.security.access.annotation.Secured;

@HtmlImport("styles/shared-styles.html")
@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
public class DashboardView extends Div {


    public DashboardView() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        init();
    }

    private void init() {

        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        globalLayout.add(new H1("Dashboard"));

        HorizontalLayout rowOne = new HorizontalLayout();
        rowOne.setClassName("dashboard-row-one");
        VerticalLayout planOverviewLayoutLayout = getPlanOverviewLayout();
        planOverviewLayoutLayout.setClassName("dashboard-tile");
        VerticalLayout notificationLayout = getNotificationLayout();
        notificationLayout.setClassName("dashboard-tile");
        rowOne.add(planOverviewLayoutLayout, notificationLayout);

        HorizontalLayout rowTwo = new HorizontalLayout();
        rowTwo.setClassName("dashboard-row-two");
        VerticalLayout upcomingEventsLayout = getUpcomingEventsLayout();
        upcomingEventsLayout.setClassName("dashboard-tile");
        rowTwo.add(upcomingEventsLayout);

        globalLayout.add(rowOne, rowTwo);

        this.add(globalLayout);

    }

    private VerticalLayout getPlanOverviewLayout() {
        VerticalLayout overviewLayout = new VerticalLayout();
        overviewLayout.setAlignSelf(FlexComponent.Alignment.START);
        overviewLayout.add(new Span("Plan Overview"));
        return overviewLayout;
    }

    private VerticalLayout getNotificationLayout() {
        VerticalLayout notificationLayout = new VerticalLayout();
        notificationLayout.setAlignSelf(FlexComponent.Alignment.END);
        notificationLayout.add(new Span("Notifications"));
        return notificationLayout;
    }

    private VerticalLayout getUpcomingEventsLayout() {
        VerticalLayout eventsLayout = new VerticalLayout();
        eventsLayout.add(new Span("Upcoming Events"));
        return eventsLayout;
    }

}
