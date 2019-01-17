package com.volavis.veraplan.spring.views;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.*;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.email.EmailSendService;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.planimport.ImportService;
import com.volavis.veraplan.spring.planimport.model.ImportAssignment;
import com.volavis.veraplan.spring.planimport.model.ImportTimeslot;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.components.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.vaadin.stefan.dnd.DndActivator;
import org.vaadin.stefan.dnd.drag.DragSourceExtension;
import org.vaadin.stefan.dnd.drop.DropTargetExtension;

import java.util.*;

@PageTitle("Veraplan - Plan")
@HtmlImport("styles/shared-styles.html")
@Route(value = "plan", layout = MainLayout.class)
public class PlanView extends Div {

    private static final Logger logger = LoggerFactory.getLogger(PlanView.class);

    private ImportService importService;
    private EmailSendService mailService;
    private User currentUser;
    private AssComponent currentlyDragged;
    private UserService userService;
    private int timeslotCount = 7;


    @Autowired
    public PlanView(UserService userService, ImportService importService, EmailSendService mailService) {

        this.userService = userService;
        this.importService = importService;
        this.mailService = mailService;
        this.currentUser = userService.getByUsernameOrEmail(SecurityUtils.getUsername());

        DndActivator.activateMobileDnd();

        buildNonCollab();

        //---------------------------------------


        //activate drag&drop support


//        toolkit = new CollaborationToolkit(userService, "1337");

//        gloablLayout = new VerticalLayout();

//        toolkit.addAssignmentDragDropEventListener(this::receiveAssignmentMoveEvent);

//        toolkit.add(gloablLayout);
//        this.add(toolkit);
    }

//    private void sendAssignmentMoveEvent(AssignmentComponentMoveEvent event) {
//        toolkit.sendDragDropEvent(event);
//    }

//    private void receiveAssignmentMoveEvent(AssignmentComponentMoveEvent event) {
//
//        AssignmentContainer source = model.values().stream().filter(container -> container.containsAssignmentComponent(event.getComponentAssignmentId())).findFirst().get();
//
////        AssignmentContainer source = model.get(event.getSourceTimeslot(), event.getSourceWeekday());
//        AssignmentContainer target = model.get(event.getTargetTimeslot(), event.getTargetWeekday());
//        AssignmentComponent component = source.getAssignmentComponentFromAssignmentId(event.getComponentAssignmentId()).orElse(null);
//
//        logger.info("receiving: " + event.toString() + " C=" + component);
//        //check valid event
//        if (component != null) {
//            source.removeAssignmentComponent(component);
//            target.addAssignmentComponent(component);
//            component.setParentContainer(target);
//
//            renderPlanModel();
//        }
//    }

//    private void renderPlanModel() {
//        planGrid.removeAll();
//
//        //setup container
//        planGrid.setWidth("100%");
//        planGrid.getStyle().set("display", "grid");
//        planGrid.getStyle().set("grid-template-columns", "repeat(8,auto)");
//        planGrid.getStyle().set("grid-gap", "10px 10px");
//
//
//        // render top row
//
//        ViewHelper.setupWeekCalendar(planGrid, timeslotCount);
//
////        Span timeslotLabel = new Span("Timeslot");
////        timeslotLabel.getStyle().set("grid-area", "1 / 1 / span 1 / span 1");
////        Span mondayLabel = new Span("Monday");
////        mondayLabel.getStyle().set("grid-area", "1 / 2 / span 1 / span 1");
////        Span tuesdayLabel = new Span("Tuesday");
////        tuesdayLabel.getStyle().set("grid-area", "1 / 3 / span 1 / span 1");
////        Span wednesdayLabel = new Span("Wednesday");
////        wednesdayLabel.getStyle().set("grid-area", "1 / 4 / span 1 / span 1");
////        Span thursdayLabel = new Span("Thursday");
////        thursdayLabel.getStyle().set("grid-area", "1 / 5 / span 1 / span 1");
////        Span fridayLabel = new Span("Friday");
////        fridayLabel.getStyle().set("grid-area", "1 / 6 / span 1 / span 1");
////        Span saturdayLabel = new Span("Saturday");
////        saturdayLabel.getStyle().set("grid-area", "1 / 7 / span 1 / span 1");
////        Span sundayLabel = new Span("Sunday");
////        sundayLabel.getStyle().set("grid-area", "1 / 8 / span 1 / span 1");
////
////        Span horizontalLine = new Span();
////        horizontalLine.getStyle().set("grid-area", "2 / 1 / span 1 / span 8");
////        horizontalLine.getStyle().set("border-top", "1px solid");
////
////        planGrid.setComponent(timeslotLabel, mondayLabel, tuesdayLabel, wednesdayLabel,
////                thursdayLabel, fridayLabel, saturdayLabel, sundayLabel, horizontalLine);
////
////        //render timeslots
////
////        for (int i = 1; i <= timeslotCount; i++) {
////            Span tsLabel = new Span(Integer.toString(i));
////            tsLabel.getStyle().set("grid-area", (i + 2) + " / 1 / span 1 / span 1");
////            planGrid.setComponent(tsLabel);
////        }
//
//        //setComponent assignments
//
//        for (MultiKey<? extends Integer> entry : model.keySet()) { //model = timeslot x weekday --> assignmentcontainer
//            AssignmentContainer assignmentContainer = model.get(entry);
//            assignmentContainer.getStyle().set("grid-area", (entry.getKey(0) + 2) + " / " + (entry.getKey(1) + 1) + " /span 1 / span 1");
//            planGrid.add(assignmentContainer);
//        }
//
//
//    }

//    private void buildModel(List<Assignment> assignments) {
////        MultiKeyMap<Integer, AssignmentContainer> model =
//
//        //prefill model with empty assignment-components
//        for (int ts = 1; ts <= timeslotCount; ts++) {
//            for (int weekday = 1; weekday <= 7; weekday++) {
//                AssignmentContainer container = new AssignmentContainer(ts, weekday);
//
//                //Drag&Drop Target logic: every assignmentContainer can be a target, but only assigned Assignments can be source
//                DropTargetExtension<AssignmentContainer> target = DropTargetExtension.extend(container);
//
//                target.addDropListener(event -> {
////                    Notification.show("drop-into ["
////                            + event.getComponent().getTimeslotEnumerator() +
////                            "," + event.getComponent().getWeekday() + "] !");
//
//                    AssignmentComponentMoveEvent acme = new AssignmentComponentMoveEvent(currentlyDraggedComponent.getAssignment().getId(),
//                            event.getComponent().getTimeslotEnumerator(),
//                            event.getComponent().getWeekday());
//
////                    logger.info("sending: " + acme.toString());
//                    sendAssignmentMoveEvent(acme);
//
//                    currentlyDraggedComponent.getParentContainer().removeAssignmentComponent(currentlyDraggedComponent);
//                    currentlyDraggedComponent.setParentContainer(event.getComponent());
//                    event.getComponent().addAssignmentComponent(currentlyDraggedComponent);
//
////                    event.getComponent().render();
//
//
//                });
//                model.put(ts, weekday, container);
//            }
//        }
//        //setComponent assignments to specific assignment components
//        for (Assignment assignment : assignments) {
//            int tsEnum = ViewHelper.getAssignmentTimeSlotSmallestEnumerator(assignment);
//            int dayOfWeek = ViewHelper.getAssignmentDayOfWeek(assignment);
//
//            AssignmentContainer assignmentContainer = model.get(tsEnum, dayOfWeek);
//
//            AssignmentComponent assignmentComponent = new AssignmentComponent(assignment, assignmentContainer);
//
//            assignmentContainer.addAssignmentComponent(assignmentComponent);
//
//            //Drag&Drop Source logic
//            DragSourceExtension<AssignmentComponent> source = DragSourceExtension.extend(assignmentComponent);
//
//            source.addDragStartListener(event -> {
////                Notification.show("drag-start ["
////                        + ViewHelper.getAssignmentTimeSlotSmallestEnumerator(event.getComponent().getAssignment()) +
////                        "," + ViewHelper.getAssignmentDayOfWeek(event.getComponent().getAssignment()) + "] !");
//                this.currentlyDraggedComponent = event.getComponent();
//            });
//
//
//        }
//    }

//    private void initView(VerticalLayout globalLayout) {
//
//
//        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
////        globalLayout.setComponent(new H1("Headline"));
//
//        buildModel(readPlansFromQuery());
//        renderPlanModel();
//
//
//        // Save + Cancel
//
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        Button saveChanges = new Button("Save", event -> {
//            //TODO: implement this - save changes to new plan
//        });
//        Button cancelChanges = new Button("Cancel", buttonClickEvent -> {
//            //reset changes:
//            buildModel(readPlansFromQuery());
//            renderPlanModel();
//        });
//        buttonLayout.add(saveChanges, cancelChanges);
//
//
//        globalLayout.add(planGrid);
//        globalLayout.add(buttonLayout);
//    }


//    private List<Assignment> readPlansFromQuery() {
//        logger.info("parameters: " + this.queryParameters);
//
//        if (this.queryParameters.containsKey("collaboration")) {
//            List<String> collaborationParameters = queryParameters.get("collaboration");
//            return getCollaborationPlan();
//        }
//        return getOwnPlan();
//    }

//    private List<Assignment> getCollaborationPlan() {
//        ArrayList<Assignment> collabAssignments = new ArrayList<>();
//
//        //mark own assignments as 'own' or setComponent origin to AssignmentContainer / AssignmentComponent, setComponent to
//        //TODO
//
//        //setComponent collab assignments with own color
//
//        return collabAssignments;
//    }

//    private List<Assignment> getOwnPlan() {
//        ArrayList<Assignment> assignments = new ArrayList<>();
//
//        Assignment a1 = new Assignment();
//        a1.setId(1L);
//        Usergroup ug1 = new Usergroup();
//        ug1.setName("4a");
//        a1.setUsergroups(Collections.singletonList(ug1));
//        Assignment a2 = new Assignment();
//        a2.setId(2L);
//        a2.setName("a2");
//
//        List<TimeSlot> a1ts = new ArrayList<>();
//        List<TimeSlot> a2ts = new ArrayList<>();
//
//        TimeSlot t1 = new TimeSlot();
//        t1.setWeekday(LocalDate.now().getDayOfWeek().getValue());
//        t1.setTimeSlotIndex(1);
//
//        TimeSlot t2 = new TimeSlot();
//        t2.setWeekday(LocalDate.now().getDayOfWeek().getValue());
//        t2.setTimeSlotIndex(3);
//
//        a1ts.add(t1);
//        a2ts.add(t2);
//
//        a1.setTimeSlots(a1ts);
//        a2.setTimeSlots(a2ts);
//
//        assignments.add(a1);
//        assignments.add(a2);
//
//        return assignments;
//    }

    private void handleMoveNonCollab(FlowTable table, AssContainer target) {
        //check collision:
        ImportTimeslot targetTimeslot = new ImportTimeslot();
        targetTimeslot.setDay(target.getCol());
        targetTimeslot.setSlot(target.getRow());
        Optional<ImportAssignment> collision = importService.checkCollision(currentUser, currentlyDragged.getAssignment(), targetTimeslot);
        collision.ifPresent(c -> {
//            ignore collision with one-self
            if (c.getTeacher().getId().equals(importService.getTeacherFromUsername(currentUser.getUsername()).get())) {
                logger.info("self-collision ignored!");
            }
//            else if (!importService.getTeacherFromUsername(c.getTeacher().getId()).isPresent()) { //check unmapped teacher
//                logger.info("unmapped teacher '" + c.getTeacher().getId() + "'!");
//            }
            else {
                User collisionPartner = userService.getByUsernameOrEmail(c.getTeacher().getId());

                String collisionPartnerFullName = collisionPartner.getFirst_name() + " " + collisionPartner.getLast_name();
                Dialog collisiondialog = ViewHelper.getConfirmationDialog("Es ist eine Kollision mit '" +
                        collisionPartnerFullName +
                        "' aufgetreten. Möchten sie den Plan kollaborativ bearbeiten?", confirmed -> {
                    //TODO navigae to PlanCollabView + send email:
                    mailService.sendCollaborationInvite(collisionPartner, currentUser);
                    Map<String, List<String>> parameterMap = new HashMap<>();
                    parameterMap.put("collaboration", Collections.singletonList("" + collisionPartner.getId()));
                    QueryParameters parameters = new QueryParameters(parameterMap);
                    this.getUI().ifPresent(ui -> ui.navigate("plancollab",parameters));
//                    Notification.show("Einladung wurde an " + collisionPartnerFullName + " gesendet.");
                }, abborted -> {
                    //undo move event
                    Optional<Component> elem = target.getChildren().filter(child -> child.equals(currentlyDragged)).findFirst();
                    elem.ifPresent(target::remove);
                    currentlyDragged.getParentContainer().add(currentlyDragged);

                });
                collisiondialog.open();
            }
        });
    }

    private void buildNonCollab() {
        this.removeAll();
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(new H1("Wochenplan für: " + currentUser.getFirst_name() + " " + currentUser.getLast_name()));

        layout.add(new Button("Vorschlag speichern"));


        FlowTable table = ViewHelper.generateWeekCalendar(timeslotCount);

        List<ImportAssignment> assignments = importService.getPersonalPlan(currentUser).getAssignments();

        //prefill calendar
        for (int ts = 1; ts <= timeslotCount; ts++) {
            for (int day = 1; day <= 5; day++) {
                AssContainer assContainer = new AssContainer(day, ts);
                table.setComponent(day + 1, ts + 1, assContainer);

                DropTargetExtension<AssContainer> target = DropTargetExtension.extend(assContainer);
                target.addDropListener(event -> handleMoveNonCollab(table, event.getComponent()));
            }
        }

        //add assignments (TODO color accordingly)
        for (ImportAssignment assignment : assignments) {
            AssContainer container = (AssContainer) table.getComponent(assignment.getTimeSlot().getDay() + 1, assignment.getTimeSlot().getSlot() + 1);
            AssComponent assComponent = new AssComponent(container);
            assComponent.setAssignment(assignment);
            container.addAssignmentComponent(assComponent);

            DragSourceExtension<AssComponent> source = DragSourceExtension.extend(assComponent);
            source.addDragStartListener(event -> {
                this.currentlyDragged = assComponent;
            });
        }

        layout.add(table);
        this.add(layout);
    }
}
