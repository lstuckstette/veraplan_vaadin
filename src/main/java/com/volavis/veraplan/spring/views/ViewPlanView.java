package com.volavis.veraplan.spring.views;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.views.components.AssignmentComponent;
import com.volavis.veraplan.spring.views.components.CollaborationToolkit;

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


        Grid<Assignment> planGrid = new Grid<>();

        planGrid.setItems(this.getMockAssignments());

        Grid.Column<Assignment> timeslot = planGrid.addComponentColumn(assignment -> {
            AssignmentComponent ac = new AssignmentComponent(assignment);
            return new Span(Integer.toString(ac.getDayOfWeek()));
        }).setHeader("Timeslot");
        Grid.Column<Assignment> monday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.MONDAY))
                .setHeader("Monday");
        Grid.Column<Assignment> tuesday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.TUESDAY))
                .setHeader("Tuesday");
        Grid.Column<Assignment> wednesday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.WEDNESDAY))
                .setHeader("Wednesday");
        Grid.Column<Assignment> thursday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.THURSDAY))
                .setHeader("Thursday");
        Grid.Column<Assignment> friday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.FRIDAY))
                .setHeader("Friday");
        Grid.Column<Assignment> saturday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.SATURDAY))
                .setHeader("Saturday");
        Grid.Column<Assignment> sunday = planGrid.addComponentColumn(assignment -> new AssignmentComponent(assignment, Calendar.SUNDAY))
                .setHeader("Sunday");


        planGrid.setColumns();

    }

    private List<Assignment> getMockAssignments() {
        ArrayList<Assignment> assignments = new ArrayList<>();
        Assignment assignment = new Assignment();
        assignment.setName("testAssign");
        List<TimeSlot> assignmentTimeSlots = new ArrayList<>();

        TimeSlot t1 = new TimeSlot();
        t1.setDate(Calendar.getInstance().getTime());


        assignmentTimeSlots.add(t1);

        assignment.setTimeSlots(assignmentTimeSlots);
        return assignments;
    }


}
