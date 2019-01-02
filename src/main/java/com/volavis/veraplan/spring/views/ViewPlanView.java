package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.views.components.AssignmentComponent;
import com.volavis.veraplan.spring.views.components.CollaborationToolkit;

import com.volavis.veraplan.spring.views.components.ViewHelper;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
        globalLayout.add(new H1("Headline"));

        // map K1 x K2 x ... x KN -> V  -->  TimeSlotIndex x Weekday -> AssignmentComponent
        MultiKeyMap<Integer, AssignmentComponent> planModel = new MultiKeyMap<>();


        Div planGrid = new Div();
        //TODO: use multikeymap as model for css-grid div...
        // fill map and call render method
        // handle map collision! (multiple assignments on same key pair...
        // create custom div-element that holds timeslot & weekday
        // ...
        planGrid.setWidth("100%");
        planGrid.getStyle().set("display", "grid");
        planGrid.getStyle().set("grid-template-columns", "repeat(8,auto)");
//        planGrid.getStyle().set("border-bottom", "1px solid");

        Span timeslotLabel = new Span("Timeslot");
        timeslotLabel.getStyle().set("grid-column", "1 / span 1");
        Span mondayLabel = new Span("Monday");
        mondayLabel.getStyle().set("grid-column", "2 / span 1");
        Span tuesdayLabel = new Span("Tuesday");
        tuesdayLabel.getStyle().set("grid-column", "3 / span 1");
        Span wednesdayLabel = new Span("Wednesday");
        wednesdayLabel.getStyle().set("grid-column", "4 / span 1");
        Span thursdayLabel = new Span("Thursday");
        thursdayLabel.getStyle().set("grid-column", "5 / span 1");
        Span fridayLabel = new Span("Friday");
        fridayLabel.getStyle().set("grid-column", "6 / span 1");
        Span saturdayLabel = new Span("Saturday");
        saturdayLabel.getStyle().set("grid-column", "7 / span 1");
        Span sundayLabel = new Span("Sunday");
        sundayLabel.getStyle().set("grid-column", "8 / span 1");

        Span headerLine = new Span();
        headerLine.getStyle().set("grid-column", "1 / span 8");
        headerLine.getStyle().set("grid-row", "2 / span 1");
        headerLine.getStyle().set("border-top", "1px solid");

        for (int i = 1; i <= 10; i++) {
            Span tsLabel = new Span(Integer.toString(i));
            tsLabel.getStyle().set("grid-column", "1 /span 1");
            tsLabel.getStyle().set("grid-row", (i + 1) + " /span 1");
            planGrid.add(tsLabel);
        }

        planGrid.add(timeslotLabel, mondayLabel, tuesdayLabel, wednesdayLabel,
                thursdayLabel, fridayLabel, saturdayLabel, sundayLabel, headerLine);

        for (Assignment a : getMockAssignments()) {
            addAssignmentToGrid(planGrid, a);
        }

        globalLayout.add(planGrid);

//        Grid<Assignment> planGrid = new Grid<>();
//        planGrid.setSelectionMode(Grid.SelectionMode.NONE);
//        planGrid.setItems(this.getMockAssignments());
//
//        Grid.Column<Assignment> timeslot = planGrid.addComponentColumn(assignment -> {
//            AssignmentComponent ac = new AssignmentComponent(assignment);
//            return new Span(Integer.toString(ac.getTimeslotIndices().get(0)));
//        }).setHeader("Timeslot");
//        Grid.Column<Assignment> monday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.MONDAY))
//                .setHeader("Monday");
//        Grid.Column<Assignment> tuesday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.TUESDAY))
//                .setHeader("Tuesday");
//        Grid.Column<Assignment> wednesday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.WEDNESDAY))
//                .setHeader("Wednesday");
//        Grid.Column<Assignment> thursday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.THURSDAY))
//                .setHeader("Thursday");
//        Grid.Column<Assignment> friday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.FRIDAY))
//                .setHeader("Friday");
//        Grid.Column<Assignment> saturday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.SATURDAY))
//                .setHeader("Saturday");
//        Grid.Column<Assignment> sunday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.SUNDAY))
//                .setHeader("Sunday");
//
//
    }

    public void addAssignmentToGrid(Div grid, Assignment assignment) {

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.getStyle().set("background", "plum");
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(new Span(assignment.getName()));
        verticalLayout.getStyle().set("grid-column", ViewHelper.getAssignmentDayOfWeek(assignment) + " / span 1");
        verticalLayout.getStyle().set("grid-row", ViewHelper.getAssignmentTimeSlotEnumerators(assignment).get(0) + 1
                + " / span " + ViewHelper.getAssignmentTimeSlotEnumerators(assignment).size());
        grid.add(verticalLayout);

    }

    private List<Assignment> getMockAssignments() {
        ArrayList<Assignment> assignments = new ArrayList<>();

        Assignment a1 = new Assignment();
        a1.setName("a1");
        Assignment a2 = new Assignment();
        a2.setName("a2");

        List<TimeSlot> a1ts = new ArrayList<>();
        List<TimeSlot> a2ts = new ArrayList<>();

        TimeSlot t1 = new TimeSlot();
        t1.setDate(Calendar.getInstance().getTime());
        t1.setEnumerator(1);

        TimeSlot t2 = new TimeSlot();
        t2.setDate(Calendar.getInstance().getTime());
        t2.setEnumerator(3);

        a1ts.add(t1);
        a2ts.add(t2);

        a1.setTimeSlots(a1ts);
        a2.setTimeSlots(a2ts);

        assignments.add(a1);
        assignments.add(a2);

        return assignments;
    }


}
