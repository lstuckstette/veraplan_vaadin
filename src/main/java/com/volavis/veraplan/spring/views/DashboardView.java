package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.volavis.veraplan.spring.MainLayout;


import com.volavis.veraplan.spring.views.components.FlowTable;
import com.volavis.veraplan.spring.views.components.ViewHelper;
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
        VerticalLayout reviewTile = getPlanReview();
        reviewTile.setClassName("dashboard-tile");
        rowTwo.add(reviewTile);

        globalLayout.add(rowOne, rowTwo);

//        FlowTable table = ViewHelper.generateWeekCalendar(6); //TODO: show current weekplan
//        globalLayout.add(table);

        this.add(globalLayout);

    }

    private VerticalLayout getPlanReview() {
        VerticalLayout reviewLayout = new VerticalLayout();
//        reviewLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        reviewLayout.add(new H3("Bewertung generierter Pläne"));

        HorizontalLayout bar = new HorizontalLayout();
        bar.setAlignItems(FlexComponent.Alignment.CENTER);

        Icon plan1 = new Icon(VaadinIcon.CALENDAR);
        Icon plan2 = new Icon(VaadinIcon.CALENDAR);
        Icon plan3 = new Icon(VaadinIcon.CALENDAR);

        bar.add(plan1, plan2, plan3);
        reviewLayout.add(bar);

        return reviewLayout;

    }

    private VerticalLayout getPlanOverviewLayout() {
        VerticalLayout overviewLayout = new VerticalLayout();


//        HorizontalLayout header = new HorizontalLayout();


        H3 headline = new H3("Persönlicher Stundenplan");

        HorizontalLayout iconLayout = new HorizontalLayout();
//        iconLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        iconLayout.setAlignSelf(FlexComponent.Alignment.CENTER);

        Icon icon = new Icon(VaadinIcon.CALENDAR_USER);
        icon.setSize("80px");
        iconLayout.add(icon);

//        calendar.addClickListener(iconClickEvent -> this.getUI().ifPresent(ui -> ui.navigate(ViewPlanView.class)));

//        header.setText();
        overviewLayout.add(headline, iconLayout);

//        overviewLayout.add(calendar);
        return overviewLayout;
    }

    private VerticalLayout getNotificationLayout() {
        VerticalLayout notificationLayout = new VerticalLayout();
//        notificationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
//        notificationLayout.setAlignSelf(FlexComponent.Alignment.END);
        notificationLayout.add(new H3("Benachrichtigungen"));
        notificationLayout.add(new Paragraph(new Icon(VaadinIcon.CARET_RIGHT), new Span("Elternsprechtag am kommenden Freitag!")));
        Hr hr = new Hr();
        hr.setWidth("100%");
        notificationLayout.add(new Hr());
        notificationLayout.add(new Paragraph(new Icon(VaadinIcon.CARET_RIGHT), new Span("Lehrerkonferenz Freitag den 01.02.2019")));
        return notificationLayout;
    }


}
