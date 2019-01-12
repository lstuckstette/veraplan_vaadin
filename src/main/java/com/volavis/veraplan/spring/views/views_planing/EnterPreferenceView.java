package com.volavis.veraplan.spring.views.views_planing;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeConstraint;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.service.TimeConstraintService;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.components.FlowTable;
import com.volavis.veraplan.spring.views.components.ViewHelper;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@PageTitle("Veraplan - Freistunden")
@Route(value = "planing/preference", layout = MainLayout.class)
public class EnterPreferenceView extends Div {

    private TimeConstraintService timeConstraintService;
    //    private UserService userService;
    private FlowTable weekCalendar;
    //    private VerticalLayout addNewPreferenceComponent;
    private User currentUser;
    private int timeslotCount = 10;

    public EnterPreferenceView(@Autowired TimeConstraintService timeConstraintService, @Autowired UserService userService) {

//        this.userService = userService;
        this.timeConstraintService = timeConstraintService;
        weekCalendar = ViewHelper.generateWeekCalendar(timeslotCount);
        this.currentUser = userService.getSingleUser(SecurityUtils.getUsername());
//        addNewPreferenceComponent = buildAddNewPreferenceComponent();
        renderWeekCalendar();
        initView();
    }

    private void initView() {
        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        globalLayout.add(new H1("Verwaltung gewünschter Freistunden"));

        globalLayout.add(weekCalendar);
        this.add(globalLayout);
    }

    private void renderWeekCalendar() {
//        weekCalendar.removeAllComponents();
        //build model:

        List<TimeConstraint> timeConstraints = timeConstraintService.getAllFromUser(currentUser);

        for (TimeConstraint tc : timeConstraints) {

        }

        for (int ts = 1; ts <= timeslotCount; ts++) {
            for (int weekday = 1; weekday <= 5; weekday++) {
                weekCalendar.add(weekday + 1, ts + 1, new PreferenceComponent(ts, weekday));
            }
        }

        for (TimeConstraint tc : timeConstraints) {
            Optional<TimeSlot> optionalTimeSlot = timeConstraintService.getTimeSlots(tc).stream().findFirst();
            if (optionalTimeSlot.isPresent()) {
                TimeSlot timeSlot = optionalTimeSlot.get();
                PreferenceComponent c = (PreferenceComponent) weekCalendar.getComponent(timeSlot.getWeekday(), timeSlot.getTimeSlotIndex()); //TODO: unchecked cast!
                c.setTimeConstraint(tc);
            }
        }

        for (int ts = 1; ts <= timeslotCount; ts++) {
            for (int weekday = 1; weekday <= 5; weekday++) {
                PreferenceComponent preferenceComponent = (PreferenceComponent) weekCalendar.getComponent(weekday+1, ts+1); //TODO: unchecked cast!
                //Add Context-Menu with Edit/Delete
                ContextMenu contextMenu = new ContextMenu();
                contextMenu.setTarget(preferenceComponent);
                if (preferenceComponent.isEmpty()) {
                    contextMenu.addItem("Hinzufügen", menuItemClickEvent -> {
                        Dialog addDialog = this.getEditOrAddPreferenceDialog(false, preferenceComponent);
                        addDialog.open();
                    });
                } else {
                    contextMenu.addItem("Ändern", menuItemClickEvent -> {
                        Dialog editDialog = this.getEditOrAddPreferenceDialog(true, preferenceComponent);
                        editDialog.open();
                    });
                    contextMenu.addItem("Löschen", menuItemClickEvent -> {
                        Dialog confirmationDialog = ViewHelper.getConfirmationDialog("Are you sure you want to delete this Preference?", confirmed -> {
                            timeConstraintService.removeTimeConstraint(preferenceComponent.getTimeConstraint());
                            preferenceComponent.removeTimeConstraint();
                            renderWeekCalendar();
                        });
                        confirmationDialog.open();
                    });
                }
            }
        }


//        for (MultiKey<? extends Integer> entry : model.keySet()) { //model = timeslot x weekday --> assignmentcontainer
//            PreferenceComponent preferenceComponent = model.get(entry);
//            //Add Context-Menu with Edit/Delete
//            ContextMenu contextMenu = new ContextMenu();
//            contextMenu.setTarget(preferenceComponent);
//            if (preferenceComponent.isEmpty()) {
//                contextMenu.addItem("Add", menuItemClickEvent -> {
//                    Dialog addDialog = this.getEditOrAddPreferenceDialog(false, preferenceComponent, entry.getKey(0), entry.getKey(1));
//                    addDialog.open();
//                });
//            } else {
//                contextMenu.addItem("Edit", menuItemClickEvent -> {
//                    Dialog editDialog = this.getEditOrAddPreferenceDialog(true, preferenceComponent, entry.getKey(0), entry.getKey(1));
//                    editDialog.open();
//                });
//                contextMenu.addItem("Delete", menuItemClickEvent -> {
//                    Dialog confirmationDialog = ViewHelper.getConfirmationDialog("Are you sure you want to delete this Preference?", confirmed -> {
//                        timeConstraintService.removeTimeConstraint(preferenceComponent.getTimeConstraint());
//                        preferenceComponent.removeTimeConstraint();
//                        renderWeekCalendar();
//                    });
//                    confirmationDialog.open();
//                });
//            }
//        }
    }

    private Dialog getEditOrAddPreferenceDialog(boolean isEdit, PreferenceComponent component) {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        if (isEdit) {
            layout.add(new H2("Eintrag editieren: " + component.getTimeConstraint().getName()));
        } else {
            layout.add(new H2("Eintrag hinzufügen:"));
        }

        FormLayout formLayout = new FormLayout();

        ComboBox<DayOfWeek> selectWeekday = new ComboBox<>();
        selectWeekday.setRequired(true);
        selectWeekday.setRenderer(new ComponentRenderer<>(item -> {
            return new Span(item.getDisplayName(TextStyle.FULL, Locale.GERMANY));
        }));
        selectWeekday.setItems(DayOfWeek.values());
        selectWeekday.setValue(DayOfWeek.of(component.getWeekday()));

        ComboBox<Integer> selectTimeslot = new ComboBox<>();
        selectTimeslot.setRequired(true);
        selectTimeslot.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        selectTimeslot.setValue(component.getTimeslot());

        TextField preferenceName = new TextField();
        TextArea preferenceDescription = new TextArea();

        formLayout.addFormItem(selectWeekday, "Wochentag");
        formLayout.addFormItem(selectTimeslot, "Stunde");
        formLayout.addFormItem(preferenceName, "Preference Name");
        formLayout.addFormItem(preferenceDescription, "Beschreibung");


        if (isEdit) {
            //prefill form-items with according data:
            selectTimeslot.setValue(component.getTimeConstraint().getTimeSlots().get(0).getTimeSlotIndex()); //TODO fix get(0)
            selectWeekday.setValue(DayOfWeek.of(component.getTimeConstraint().getTimeSlots().get(0).getWeekday()));
            preferenceName.setValue(component.getTimeConstraint().getName());
            preferenceDescription.setValue(component.getTimeConstraint().getDescription());
        }

        HorizontalLayout buttonBar = new HorizontalLayout();
        Button save = new Button("Save", buttonClickEvent -> {

            //Create new TimeConstraint
            Dialog warning = ViewHelper.getConfirmationDialog("Do you really want to save entered data?", confirmed -> {

                if (isEdit) {
                    timeConstraintService.saveChanges(component.getTimeConstraint(), selectTimeslot.getValue(),
                            selectWeekday.getValue(), preferenceName.getValue(), preferenceDescription.getValue());

                } else {
                    timeConstraintService.createTimeConstraint(selectTimeslot.getValue(), selectWeekday.getValue(),
                            preferenceName.getValue(), preferenceDescription.getValue(), currentUser);
                }
                renderWeekCalendar();
                dialog.close();
            });
            warning.open();
        });
        Button reset = new Button("Reset", buttonClickEvent -> {
            if (isEdit) {
                selectTimeslot.setValue(component.getTimeConstraint().getTimeSlots().get(0).getTimeSlotIndex()); //TODO fix get(0)
                selectWeekday.setValue(DayOfWeek.of(component.getTimeConstraint().getTimeSlots().get(0).getWeekday()));
                preferenceName.setValue(component.getTimeConstraint().getName());
                preferenceDescription.setValue(component.getTimeConstraint().getDescription());
            } else {
                selectWeekday.setValue(DayOfWeek.MONDAY);
                selectTimeslot.setValue(1);
                preferenceName.clear();
                preferenceDescription.clear();
            }

        });
        Button cancel = new Button("Cancel", event -> {
            dialog.close();
        });

        buttonBar.add(save, reset, cancel);

        layout.add(formLayout);
        layout.add(buttonBar);


        dialog.add(layout);
        return dialog;
    }


    static class PreferenceComponent extends VerticalLayout {
        private TimeConstraint timeConstraint;

        private int timeslot, weekday;

        public PreferenceComponent(int timeslot, int weekday) {
            this.timeslot = timeslot;
            this.weekday = weekday;
            buildComponent();
        }

        public PreferenceComponent(TimeConstraint timeConstraint) {
            this.timeConstraint = timeConstraint;
            buildComponent();
        }

        void setTimeConstraint(TimeConstraint timeConstraint) {
            this.timeConstraint = timeConstraint;
            buildComponent();
        }

        boolean isEmpty() {
            return this.timeConstraint == null;
        }

        TimeConstraint getTimeConstraint() {
            return timeConstraint;
        }

        void removeTimeConstraint() {
            this.timeConstraint = null;
            buildComponent();
        }

        public int getTimeslot() {
            return timeslot;
        }

        public void setTimeslot(int timeslot) {
            this.timeslot = timeslot;
        }

        public int getWeekday() {
            return weekday;
        }

        public void setWeekday(int weekday) {
            this.weekday = weekday;
        }

        private void buildComponent() {
            this.removeAll();
            this.setAlignItems(Alignment.CENTER);
            if (timeConstraint != null) {
//                this.getStyle().remove("border");
                this.add(new Span(timeConstraint.getName()));
            } else {
//                this.getStyle().set("border", "1px dotted");
            }
        }
    }

}
