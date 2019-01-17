package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.volavis.veraplan.spring.MainLayout;


import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.ressources.Planrating;
import com.volavis.veraplan.spring.persistence.service.PlanratingService;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.components.RatingComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@HtmlImport("styles/shared-styles.html")
@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
public class DashboardView extends Div implements HasUrlParameter<String> {

    private PlanratingService planratingService;
    private User currentUser;

    public DashboardView(@Autowired UserService userService, @Autowired PlanratingService planratingService) {

        this.planratingService = planratingService;
        this.currentUser = userService.getByUsernameOrEmail(SecurityUtils.getUsername());

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
        bar.setClassName("plan-review-container");
        bar.setAlignItems(FlexComponent.Alignment.CENTER);

        for (int i = 1; i <= 3; i++) {
            VerticalLayout planLayout = new VerticalLayout();
            planLayout.setClassName("plan-review");
            Span header = new Span("Plan " + i);
            Icon planIcon = new Icon(VaadinIcon.CALENDAR);
            planIcon.setClassName("plan-review-icon");
            String target = "" + i;
            planIcon.addClickListener(click -> this.getUI().ifPresent(ui -> {
                ui.navigate(PlanRateView.class, "review=" + target);
            }));
            RatingComponent stars = new RatingComponent(); //TODO: read existing rating from DB and set to component!
            int rating = planratingService.getSingleRating(currentUser, i - 1).orElse(new Planrating(0)).getRating();
            stars.setRating(rating);
            planLayout.add(header, planIcon, stars);
            bar.add(planLayout);
        }

        reviewLayout.add(bar);

        return reviewLayout;

    }

    private VerticalLayout getPlanOverviewLayout() {
        VerticalLayout overviewLayout = new VerticalLayout();

        H3 headline = new H3("Persönlicher Stundenplan");

        VerticalLayout iconLayout = new VerticalLayout();
        iconLayout.setClassName("personal-plan-container");

        Icon icon = new Icon(VaadinIcon.CALENDAR_USER);

        icon.addClickListener(iconClickEvent -> this.getUI().ifPresent(ui -> ui.navigate(PlanView.class)));

        iconLayout.add(icon);
        overviewLayout.add(headline, iconLayout);

        return overviewLayout;
    }

    private VerticalLayout getNotificationLayout() {
        VerticalLayout notificationLayout = new VerticalLayout();
        notificationLayout.add(new H3("Benachrichtigungen"));
        notificationLayout.add(new Paragraph(new Icon(VaadinIcon.CARET_RIGHT), new Span("Elternsprechtag am kommenden Freitag!")));
        Hr hr = new Hr();
        hr.setWidth("100%");
        notificationLayout.add(new Hr());
        notificationLayout.add(new Paragraph(new Icon(VaadinIcon.CARET_RIGHT), new Span("Lehrerkonferenz Freitag, den 01.02.2019")));
        return notificationLayout;
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        //TODO remove
    }
}
