package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.views.components.AssignmentComponent;
import com.volavis.veraplan.spring.views.components.AssignmentContainer;
import com.volavis.veraplan.spring.views.components.CollaborationToolkit;

import com.volavis.veraplan.spring.views.components.ViewHelper;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.dnd.DndActivator;
import org.vaadin.stefan.dnd.drag.DragSourceExtension;
import org.vaadin.stefan.dnd.drop.DropTargetExtension;

import java.util.*;

@PageTitle("Plan")
@Route(value = "plan", layout = MainLayout.class)
//@JavaScript("bower_components/interactjs/interact.min.js")
public class ViewPlanView extends Div {

    private AssignmentComponent currentlyDraggedComponent;


    @Autowired
    public ViewPlanView(UserService userService) {

        //activate drag&drop support
        DndActivator.activateMobileDnd();

        CollaborationToolkit toolkit = new CollaborationToolkit(userService, "1337");

        VerticalLayout gloablLayout = new VerticalLayout();

        initView(gloablLayout);

        toolkit.add(gloablLayout);
        this.add(toolkit);
    }

    private Div renderPlanModel(MultiKeyMap<Integer, AssignmentContainer> model, int timeslotCount) {
        Div container = new Div();

        //setup container
        container.setWidth("100%");
        container.getStyle().set("display", "grid");
        container.getStyle().set("grid-template-columns", "repeat(8,auto)");
        container.getStyle().set("grid-gap", "10px 10px");



        // render top row

        Span timeslotLabel = new Span("Timeslot");
        timeslotLabel.getStyle().set("grid-area", "1 / 1 / span 1 / span 1");
        Span mondayLabel = new Span("Monday");
        mondayLabel.getStyle().set("grid-area", "1 / 2 / span 1 / span 1");
        Span tuesdayLabel = new Span("Tuesday");
        tuesdayLabel.getStyle().set("grid-area", "1 / 3 / span 1 / span 1");
        Span wednesdayLabel = new Span("Wednesday");
        wednesdayLabel.getStyle().set("grid-area", "1 / 4 / span 1 / span 1");
        Span thursdayLabel = new Span("Thursday");
        thursdayLabel.getStyle().set("grid-area", "1 / 5 / span 1 / span 1");
        Span fridayLabel = new Span("Friday");
        fridayLabel.getStyle().set("grid-area", "1 / 6 / span 1 / span 1");
        Span saturdayLabel = new Span("Saturday");
        saturdayLabel.getStyle().set("grid-area", "1 / 7 / span 1 / span 1");
        Span sundayLabel = new Span("Sunday");
        sundayLabel.getStyle().set("grid-area", "1 / 8 / span 1 / span 1");

        Span horizontalLine = new Span();
        horizontalLine.getStyle().set("grid-area", "2 / 1 / span 1 / span 8");
        horizontalLine.getStyle().set("border-top", "1px solid");

        container.add(timeslotLabel, mondayLabel, tuesdayLabel, wednesdayLabel,
                thursdayLabel, fridayLabel, saturdayLabel, sundayLabel, horizontalLine);

        //render timeslots

        for (int i = 1; i <= timeslotCount; i++) {
            Span tsLabel = new Span(Integer.toString(i));
            tsLabel.getStyle().set("grid-area", (i + 2) + " / 1 / span 1 / span 1");
            container.add(tsLabel);
        }

        //add assignments

        for (MultiKey<? extends Integer> entry : model.keySet()) { //model = timeslot x weekday --> assignmentcontainer
            AssignmentContainer assignmentContainer = model.get(entry);
            assignmentContainer.getStyle().set("grid-area", (entry.getKey(0) + 2) + " / " + (entry.getKey(1) + 1) + " /span 1 / span 1");


            container.add(assignmentContainer);
        }

        return container;
    }

    private MultiKeyMap<Integer, AssignmentContainer> buildModel(List<Assignment> assignments, int timeslotCount) {
        MultiKeyMap<Integer, AssignmentContainer> model = new MultiKeyMap<>();

        //prefill model with empty assignment-components
        for (int ts = 1; ts <= timeslotCount; ts++) {
            for (int weekday = 1; weekday <= 7; weekday++) {
                AssignmentContainer container = new AssignmentContainer(ts, weekday);

                //Drag&Drop Target logic: every assignmentContainer can be a target, but only assigned Assignments can be source
                DropTargetExtension<AssignmentContainer> target = DropTargetExtension.extend(container);
                target.addDropListener(event -> {
                    Notification.show("drop-into ["
                            + event.getComponent().getTimeslotEnumerator() +
                            "," + event.getComponent().getWeekday() + "] !");
                    currentlyDraggedComponent.getParentContainer().removeAssignmentComponent(currentlyDraggedComponent);
                    currentlyDraggedComponent.setParentContainer(event.getComponent());
                    event.getComponent().addAssignmentComponent(currentlyDraggedComponent);
                    event.getComponent().render();

//                    this.currentlyDraggedComponent = null;
                });
                model.put(ts, weekday, container);
            }
        }
        //add assignments to specific assignment components
        for (Assignment assignment : assignments) {
            int tsEnum = ViewHelper.getAssignmentTimeSlotSmallestEnumerator(assignment);
            int dayOfWeek = ViewHelper.getAssignmentDayOfWeek(assignment);

            AssignmentContainer assignmentContainer = model.get(tsEnum, dayOfWeek);

            AssignmentComponent assignmentComponent = new AssignmentComponent(assignment, assignmentContainer);

            assignmentContainer.addAssignmentComponent(assignmentComponent);

            //Drag&Drop Source logic
            DragSourceExtension<AssignmentComponent> source = DragSourceExtension.extend(assignmentComponent);

            source.addDragStartListener(event -> {
                Notification.show("drag-start ["
                        + ViewHelper.getAssignmentTimeSlotSmallestEnumerator(event.getComponent().getAssignment()) +
                        "," + ViewHelper.getAssignmentDayOfWeek(event.getComponent().getAssignment()) + "] !");
                this.currentlyDraggedComponent = event.getComponent();
            });


        }
        return model;
    }

    private void initView(VerticalLayout globalLayout) {


        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        globalLayout.add(new H1("Headline"));

        int timeslotCount = 10;

        Div planGrid = renderPlanModel(buildModel(getMockAssignments(), timeslotCount), timeslotCount);


        // map K1 x K2 x ... x KN -> V  -->  TimeSlotIndex x Weekday -> AssignmentComponent


        globalLayout.add(planGrid);
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
