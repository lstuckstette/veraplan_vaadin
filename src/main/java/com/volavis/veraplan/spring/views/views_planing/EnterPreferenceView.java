package com.volavis.veraplan.spring.views.views_planing;

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
import com.volavis.veraplan.spring.views.components.ViewHelper;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@PageTitle("Veraplan - Enter Preferences")
@Route(value = "planing/preference", layout = MainLayout.class)
public class EnterPreferenceView extends Div {

    private TimeConstraintService timeConstraintService;
    //    private UserService userService;
    private Div weekCalendar;
    //    private VerticalLayout addNewPreferenceComponent;
    private User currentUser;
    private int timeslotCount = 10;

    public EnterPreferenceView(@Autowired TimeConstraintService timeConstraintService, @Autowired UserService userService) {

//        this.userService = userService;
        this.timeConstraintService = timeConstraintService;
        weekCalendar = new Div();
        this.currentUser = userService.getSingleUser(SecurityUtils.getUsername());
//        addNewPreferenceComponent = buildAddNewPreferenceComponent();
        renderWeekCalendar();
        initView();
    }

    private void initView() {
        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        globalLayout.add(new H1("Enter personal preferences"));

        globalLayout.add(weekCalendar);
        this.add(globalLayout);
    }

    private void renderWeekCalendar() {
        weekCalendar.removeAll();
        //build model:
        MultiKeyMap<Integer, PreferenceComponent> model = new MultiKeyMap<>();
        List<TimeConstraint> timeConstraints = timeConstraintService.getAllFromUser(currentUser);

        for (int ts = 1; ts <= timeslotCount; ts++) {
            for (int weekday = 1; weekday <= 7; weekday++) {
                model.put(ts, weekday, new PreferenceComponent());
            }
        }

        for (TimeConstraint tc : timeConstraints) {
            Optional<TimeSlot> optionalTimeSlot = timeConstraintService.getTimeSlots(tc).stream().findFirst();
            if (optionalTimeSlot.isPresent()) {
                TimeSlot timeSlot = optionalTimeSlot.get();
                model.get(timeSlot.getTimeSlotIndex(), timeSlot.getWeekday()).setTimeConstraint(tc);
//                model.put(timeSlot.getTimeSlotIndex(), timeSlot.getWeekday(), new PreferenceComponent(tc));
            }
        }

//        weekCalendar.setWidth("100%");
        weekCalendar.getStyle().set("display", "grid");
        weekCalendar.getStyle().set("grid-template-columns", "repeat(8, 1fr)");
        weekCalendar.getStyle().set("grid-gap", "10px 10px");

        ViewHelper.setupWeekCalendar(weekCalendar, timeslotCount);

        for (MultiKey<? extends Integer> entry : model.keySet()) { //model = timeslot x weekday --> assignmentcontainer
            PreferenceComponent preferenceComponent = model.get(entry);
            //Add Context-Menu with Edit/Delete
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setTarget(preferenceComponent);
            if (preferenceComponent.isEmpty()) {
                contextMenu.addItem("Add", menuItemClickEvent -> {
                    Dialog addDialog = this.getEditOrAddPreferenceDialog(false, preferenceComponent, entry.getKey(0), entry.getKey(1));
                    addDialog.open();
                });
            } else {
                contextMenu.addItem("Edit", menuItemClickEvent -> {
                    Dialog editDialog = this.getEditOrAddPreferenceDialog(true, preferenceComponent, entry.getKey(0), entry.getKey(1));
                    editDialog.open();
                });
                contextMenu.addItem("Delete", menuItemClickEvent -> {
                    Dialog confirmationDialog = ViewHelper.getConfirmationDialog("Are you sure you want to delete this Preference?", confirmed -> {
                        timeConstraintService.removeTimeConstraint(preferenceComponent.getTimeConstraint());
                        preferenceComponent.removeTimeConstraint();
                        renderWeekCalendar();
                    });
                    confirmationDialog.open();
                });
            }

            //Align in css-grid
            preferenceComponent.getStyle().set("grid-area", (entry.getKey(0) + 2) + " / " + (entry.getKey(1) + 1) + " /span 1 / span 1");
            weekCalendar.add(preferenceComponent);
        }
    }

    private Dialog getEditOrAddPreferenceDialog(boolean isEdit, PreferenceComponent component, int timeslotIndex, int weekday) {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        if (isEdit) {
            layout.add(new H2("Edit Item: " + component.getTimeConstraint().getName()));
        } else {
            layout.add(new H2("Add new Preference:"));
        }

        FormLayout formLayout = new FormLayout();

        ComboBox<DayOfWeek> selectWeekday = new ComboBox<>();
        selectWeekday.setRequired(true);
        selectWeekday.setRenderer(new ComponentRenderer<>(item -> {
            return new Span(item.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }));
        selectWeekday.setItems(DayOfWeek.values());
        selectWeekday.setValue(DayOfWeek.of(weekday));

        ComboBox<Integer> selectTimeslot = new ComboBox<>();
        selectTimeslot.setRequired(true);
        selectTimeslot.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        selectTimeslot.setValue(timeslotIndex);

        TextField preferenceName = new TextField();
        TextArea preferenceDescription = new TextArea();

        formLayout.addFormItem(selectWeekday, "Weekday");
        formLayout.addFormItem(selectTimeslot, "Timeslot");
        formLayout.addFormItem(preferenceName, "Preference Name");
        formLayout.addFormItem(preferenceDescription, "Description");


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


        PreferenceComponent() {
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

        private void buildComponent() {
            this.removeAll();
            this.setAlignItems(Alignment.CENTER);
            if (timeConstraint != null) {
                this.getStyle().remove("border");
                this.add(new Span(timeConstraint.getName()));
            } else {
                this.getStyle().set("border", "1px dotted");
            }


        }
    }

}
