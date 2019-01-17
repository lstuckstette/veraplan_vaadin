package com.volavis.veraplan.spring.views;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.ressources.Planrating;
import com.volavis.veraplan.spring.persistence.service.PlanratingService;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.planimport.ImportService;
import com.volavis.veraplan.spring.planimport.model.ImportAssignment;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.components.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Veraplan - Bewertung")
@HtmlImport("styles/shared-styles.html")
@Route(value = "rate", layout = MainLayout.class)
public class PlanRateView extends Div implements HasUrlParameter<String> {

    private static final Logger logger = LoggerFactory.getLogger(PlanRateView.class);

    private ImportService importService;
    private User currentUser;
    private PlanratingService planratingService;
    private int timeslotCount = 7;

    @Autowired
    public PlanRateView(ImportService importService, PlanratingService planratingService, UserService userService) {
        this.importService = importService;
        this.planratingService = planratingService;
        this.currentUser = userService.getByUsernameOrEmail(SecurityUtils.getUsername());
    }

    private void buildReview(String reviewPlanID) {
        int planIndex = Integer.valueOf(reviewPlanID) - 1;
        List<ImportAssignment> assignments = importService.getAlgoTeacherPlan(currentUser, planIndex);

        //headline
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new H1("Bewertung von Planoption " + reviewPlanID));

        //action-bar
        HorizontalLayout barLayout = new HorizontalLayout();
        barLayout.addClassName("review-bar-container");
        Span barText = new Span("Bitte geben Sie eine Bewertung ab: ");
        RatingComponent ratingComponent = new RatingComponent();
        //TODO: read existing rating from DB and set to component!

        int rating = planratingService.getSingleRating(currentUser, planIndex).orElse(new Planrating(0)).getRating();
        ratingComponent.setRating(rating);

        ratingComponent.activateClickToChangeRating();
        Button saveButton = new Button("Speichern", buttonClickEvent -> {
            planratingService.saveOrUpdate(currentUser, planIndex, ratingComponent.getRating());
            Notification.show("Änderung gespeichert!");
            //TODO persist rating in DB!
        });
        saveButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        barLayout.add(barText, ratingComponent, saveButton);
        layout.add(barLayout);

        //plan-table
        FlowTable table = ViewHelper.generateWeekCalendar(timeslotCount);
        //add assignments:
        for (ImportAssignment assignment : assignments) {
            //TODO: add assContainer for padding!
            AssContainer assContainer = new AssContainer(0, 0);
            assContainer.addAssignmentComponent(new AssComponent(assignment));
            table.setComponent(assignment.getTimeSlot().getDay() + 1, assignment.getTimeSlot().getSlot() + 1, assContainer);
        }
        layout.add(table);
        this.add(layout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String param) {
        if (param != null && param.matches("review=.+")) {
            buildReview(param.split("=")[1]);
        }
    }
}
