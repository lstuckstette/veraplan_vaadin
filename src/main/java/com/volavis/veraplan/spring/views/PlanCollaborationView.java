package com.volavis.veraplan.spring.views;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.planimport.ImportService;
import com.volavis.veraplan.spring.planimport.model.ImportAssignment;
import com.volavis.veraplan.spring.planimport.model.ImportTimeslot;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.components.*;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.vaadin.stefan.dnd.drag.DragSourceExtension;
import org.vaadin.stefan.dnd.drop.DropTargetExtension;

import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Veraplan - Kollaboration")
@HtmlImport("styles/shared-styles.html")
@Route(value = "plancollab", layout = MainLayout.class)
public class PlanCollaborationView extends Div implements HasUrlParameter<String> {

    private static final Logger logger = LoggerFactory.getLogger(PlanCollaborationView.class);

    private CollaborationToolkit toolkit;
    private ImportService importService;
    private UserService userService;
    private User currentUser;
    private int timeslotCount = 6;
    private FlowTable table;

    private MultiKeyMap<Integer, List<ModelEntry>> planModel;
    private List<ImportAssignment> assignments;

    private DragObject currentlyDragged;
    private AssComponent currentlyDraggedComponent;

    public PlanCollaborationView(UserService userService, ImportService importService) {
        this.importService = importService;
        this.userService = userService;
        this.currentUser = userService.getByUsernameOrEmail(SecurityUtils.getUsername());

        planModel = new MultiKeyMap<>();
        assignments = new ArrayList<>();
        table = ViewHelper.generateWeekCalendar(timeslotCount);
    }

    private void buildCollab(List<String> collaborators) {
        toolkit = new CollaborationToolkit(userService, "1337");
        toolkit.addAssignmentDragDropEventListener(event -> {
            this.renderAssMoveEvent(table, event, false);
        });


        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(new H1("Kollaboratives  Bearbeiten der Pläne (Tausch)"));


        List<ImportAssignment> ownAssignments = importService.getMockTeacherPlan(0, currentUser);


        Optional<User> collaborator = userService.getById(collaborators.get(0));
        List<ImportAssignment> collaboratorAssignments = new ArrayList<>();
        collaborator.ifPresent(c -> collaboratorAssignments.addAll(importService.getMockTeacherPlan(0, c)));

        for (ImportAssignment own : ownAssignments) {
            if (planModel.containsKey(own.getTimeSlot().getDay(), own.getTimeSlot().getSlot())) {
                //collection already exists, adding to it;
                List<ModelEntry> mel = planModel.get(own.getTimeSlot().getDay(), own.getTimeSlot().getSlot());
                mel.add(new ModelEntry(true, own.getAssignmentNumber()));
            } else {
                //create
                List<ModelEntry> list = new ArrayList<>();
                list.add(new ModelEntry(true, own.getAssignmentNumber()));
                planModel.put(own.getTimeSlot().getDay(), own.getTimeSlot().getSlot(), list);
            }
        }

        for (ImportAssignment foreign : collaboratorAssignments) {
            if (planModel.containsKey(foreign.getTimeSlot().getDay(), foreign.getTimeSlot().getSlot())) {
                List<ModelEntry> mel = planModel.get(foreign.getTimeSlot().getDay(), foreign.getTimeSlot().getSlot());
                mel.add(new ModelEntry(false, foreign.getAssignmentNumber()));
            } else {
                ArrayList<ModelEntry> list = new ArrayList<>();
                list.add(new ModelEntry(false, foreign.getAssignmentNumber()));
                planModel.put(foreign.getTimeSlot().getDay(), foreign.getTimeSlot().getSlot(),
                        list);
            }
        }

        assignments.addAll(ownAssignments);
        assignments.addAll(collaboratorAssignments);

        renderModel(planModel);

//        //add own assignments:
//        for (ImportAssignment own : ownAssignments) {
//            AssContainer container = new AssContainer(own.getTimeSlot().getDay(), own.getTimeSlot().getSlot());
//            AssComponent component = new AssComponent(container);
//            component.setAssignment(own);
//            component.setOwn();
//            container.addAssignmentComponent(component);
//
//            table.setComponent(own.getTimeSlot().getDay() + 1, own.getTimeSlot().getSlot() + 1, container);
//
//            DragSourceExtension<AssComponent> source = DragSourceExtension.extend(component);
//            source.addDragStartListener(event -> {
//                this.currentlyDragged = component;
//            });
//        }
//
//        //add collaborators assignments
//        for (List<ImportAssignment> assignments : collaboratorAssignments) {
//            for (ImportAssignment foreign : assignments) {
//                AssContainer container = (AssContainer) table.getComponent(foreign.getTimeSlot().getDay() + 1, foreign.getTimeSlot().getSlot() + 1);
//                if (container == null) {
//                    container = new AssContainer(foreign.getTimeSlot().getDay(), foreign.getTimeSlot().getSlot());
//                }
//                AssComponent component = new AssComponent(container);
//                component.setAssignment(foreign);
//                component.setForeign();
//                container.addAssignmentComponent(component);
//
//                table.setComponent(foreign.getTimeSlot().getDay() + 1, foreign.getTimeSlot().getSlot() + 1, container);
//
//                DropTargetExtension<AssComponent> target = DropTargetExtension.extend(component);
//                target.addDropListener(event -> handleMoveCollab(table, event.getComponent()));
//            }
//        }
        layout.add(table);

        Button save = new Button("Vorschlag speichern");
        save.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        layout.add(save);

        toolkit.add(layout);
        this.add(toolkit);
    }

    private void renderModel(MultiKeyMap<Integer, List<ModelEntry>> model) {
        //clear table:

        table = ViewHelper.generateWeekCalendar(timeslotCount);
        for (int day = 1; day <= 5; day++) {
            for (int slot = 1; slot <= timeslotCount; slot++) {
                if (model.containsKey(day, slot)) {
                    List<ModelEntry> entryList = model.get(day, slot);
                    AssContainer container = new AssContainer(day, slot);
                    for (ModelEntry entry : entryList) {
                        List<ImportAssignment> assignmentList = assignments.stream()
                                .filter(item -> item.getAssignmentNumber() == entry.getAssignmentId())
                                .collect(Collectors.toList());
                        for (ImportAssignment current : assignmentList) {
                            AssComponent component = new AssComponent(current);
                            component.setParentContainer(container);
                            container.addAssignmentComponent(component);

                            if (entry.isOwn) {
                                component.setOwn();

                                DragSourceExtension<AssComponent> source = DragSourceExtension.extend(component);
                                final int finalDay = day;
                                final int finalSlot = slot;
                                source.addDragStartListener(event -> {
                                    this.currentlyDraggedComponent = event.getComponent();
                                    this.currentlyDragged = new DragObject(finalDay, finalSlot, component.getAssignment().getAssignmentNumber());
                                });
                            } else {
                                component.setForeign();
                                final int finalDay = day;
                                final int finalSlot = slot;
                                DropTargetExtension<AssComponent> target = DropTargetExtension.extend(component);
                                target.addDropListener(event -> {
                                    DragObject dragObject = new DragObject(finalDay, finalSlot, component.getAssignment().getAssignmentNumber());
                                    handleMoveCollab(event.getComponent(), dragObject);
                                });
                            }
                        }
                    }
                    table.setComponent(day + 1, slot + 1, container);
                }
            }
        }
    }

    private void handleMoveCollab(AssComponent targetComponent, DragObject target) {

        //register swap in model:

        List<ModelEntry> sourceContainer = planModel.get(currentlyDragged.getDay(), currentlyDragged.getSlot());
        List<ModelEntry> targetContainer = planModel.get(target.getDay(), target.getSlot());


        Optional<ModelEntry> sourceEntry = sourceContainer.stream()
                .filter(entry -> entry.getAssignmentId() == currentlyDragged.getAssignmentId()).findFirst();

        Optional<ModelEntry> targetEntry = targetContainer.stream()
                .filter(entry -> entry.getAssignmentId() == target.getAssignmentId()).findFirst();

        if (sourceEntry.isPresent() && targetEntry.isPresent()) {
            ModelEntry sourceE = sourceEntry.get();
            ModelEntry targetE = targetEntry.get();
            //remove
            sourceContainer.remove(sourceE);
            targetContainer.remove(targetE);
            //swap ownership
            sourceE.setOwn(!sourceE.isOwn());
            targetE.setOwn(!targetE.isOwn());
            //add back to respective containers:
            sourceContainer.add(targetE);
            targetContainer.add(sourceE);
        }


        //remove source from target (revert drop-action...)
        Optional<Component> elem = targetComponent.getChildren().filter(child -> child.equals(currentlyDraggedComponent)).findFirst();
        elem.ifPresent(targetComponent::remove);


        //send AssMoveEvent to toolkit
        AssMoveEvent event = new AssMoveEvent();
        event.setEventSourceUserId(currentUser.getId());

        //copy map so it can be serialized....
//        HashMap<MultiKey<? extends Integer>, List<PlanCollaborationView.ModelEntry>> map = new HashMap<>();
//        planModel.entrySet().stream().forEach(item -> map.put(item.getKey(), item.getValue()));
        event.setModel(planModel);
        toolkit.sendDragDropEvent(event);


        //render generated AssMoveEvent and overwrite check event-ownership (so it definitively gets drawn...)
        renderAssMoveEvent(table, event, true);


    }

    private void renderAssMoveEvent(FlowTable table, AssMoveEvent event, boolean overwriteCheck) {


        //check if we are the source of the AssMoveEvent
        if (event.getEventSourceUserId() == currentUser.getId()) {
            if (!overwriteCheck) {
                //abbort!
                return;
            }
        }

        if (!overwriteCheck) {
            //invert own <-> foreign in event.model!
            for (Map.Entry<MultiKey<? extends Integer>, List<ModelEntry>> entry : planModel.entrySet()) {
                for (ModelEntry mentry : entry.getValue()) {
                    mentry.setOwn(!mentry.isOwn());
                }
            }
        }
//        MultiKeyMap<Integer, List<ModelEntry>> tempModel = new MultiKeyMap<>();
//
//        event.getModel().entrySet().forEach(item -> tempModel.put(item.getKey(), item.getValue()));

        renderModel(event.getModel());
        this.planModel = event.getModel();

//        AssContainer sourceContainer = (AssContainer) table.getComponent(event.getSourceSlot().getDay() + 1, event.getSourceSlot().getSlot() + 1);
//        AssContainer targetContainer = (AssContainer) table.getComponent(event.getTargetSlot().getDay() + 1, event.getTargetSlot().getSlot() + 1);
//
//        Optional<AssComponent> optionalSource = sourceContainer.getAssComponents().stream()
//                .filter(assComponent -> assComponent.getAssignment().getAssignmentNumber() == event.getSourceId()).findFirst();
//        Optional<AssComponent> optionalTarget = targetContainer.getAssComponents().stream()
//                .filter(assComponent -> assComponent.getAssignment().getAssignmentNumber() == event.getTargetId()).findFirst();
//
//
//        if (optionalSource.isPresent() && optionalTarget.isPresent()) {
//            AssComponent source = optionalSource.get();
//            AssComponent target = optionalTarget.get();
//
//            //swap parents & render
//            AssContainer sourceParent = source.getParentContainer();
//            AssContainer targetParent = target.getParentContainer();
//
//            source.setParentContainer(targetParent);
//            target.setParentContainer(sourceParent);
//
//            sourceParent.removeAssignment(source);
//            targetParent.removeAssignment(target);
//            sourceParent.addAssignmentComponent(target);
//            targetParent.addAssignmentComponent(source);
//        } else {
//            logger.info("ignoring AssEvent - already swapped?");
//        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String param) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        Map<String, List<String>> params = queryParameters.getParameters();

        if (params.containsKey("collaboration") && !params.get("collaboration").isEmpty()) {
            buildCollab(params.get("collaboration"));
        }
    }

    public static class ModelEntry {
        private boolean isOwn;
        private int assignmentId;

        public ModelEntry() {
        }

        public ModelEntry(boolean isOwn, int assignmentId) {
            this.isOwn = isOwn;
            this.assignmentId = assignmentId;
        }

        public boolean isOwn() {
            return isOwn;
        }

        public void setOwn(boolean own) {
            isOwn = own;
        }

        public int getAssignmentId() {
            return assignmentId;
        }

        public void setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
        }
    }

    public static class DragObject {
        int day;
        int slot;
        int assignmentId;

        public DragObject(int day, int slot, int assignmentId) {
            this.day = day;
            this.slot = slot;
            this.assignmentId = assignmentId;
        }

        public int getDay() {
            return day;
        }

        public int getSlot() {
            return slot;
        }

        public int getAssignmentId() {
            return assignmentId;
        }
    }
}
