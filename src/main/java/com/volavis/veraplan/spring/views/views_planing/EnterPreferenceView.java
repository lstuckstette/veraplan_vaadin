package com.volavis.veraplan.spring.views.views_planing;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
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
    private UserService userService;
    private Div weekCalendar;
    private VerticalLayout addNewPreferenceComponent;
    private int timeslotCount = 10;

    public EnterPreferenceView(@Autowired TimeConstraintService timeConstraintService, @Autowired UserService userService) {

        this.userService = userService;
        this.timeConstraintService = timeConstraintService;
        weekCalendar = new Div();
        addNewPreferenceComponent = buildAddNewPreferenceComponent();
        renderWeekCalendar();
        initView();
    }

    private void initView() {
        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        globalLayout.add(new H1("Enter personal preference"));

        HorizontalLayout buttonBar = new HorizontalLayout();

        Button addNewPrefButton = new Button("Add new Preference");
        addNewPrefButton.addClickListener(event -> {
            addNewPreferenceComponent.setVisible(!addNewPreferenceComponent.isVisible());

            if (addNewPreferenceComponent.isVisible()) {
                addNewPrefButton.setText("Close Add new Preference");
            } else {
                addNewPrefButton.setText("Add new Preference");
            }

        });
        Button saveChangesButton = new Button("Save Changes");
        buttonBar.add(saveChangesButton, addNewPrefButton);

        globalLayout.add(buttonBar);

        HorizontalLayout contentLayout = new HorizontalLayout();

        contentLayout.add(weekCalendar, addNewPreferenceComponent);

//        contentLayout.add(weekCalendar);

        globalLayout.add(contentLayout);
        this.add(globalLayout);
    }

    private void renderWeekCalendar() { //TODO: create context-menu with delete!
        weekCalendar.removeAll();
        //build model:
        MultiKeyMap<Integer, PreferenceComponent> model = new MultiKeyMap<>();
        List<TimeConstraint> timeConstraints = userService.getTimeConstraints(userService.getSingleUser(SecurityUtils.getUsername()));

        for (int ts = 1; ts <= timeslotCount; ts++) {
            for (int weekday = 1; weekday <= 7; weekday++) {
                model.put(ts, weekday, new PreferenceComponent());
            }
        }

        for (TimeConstraint tc : timeConstraints) {
            Optional<TimeSlot> optionalTimeSlot = timeConstraintService.getTimeSlots(tc).stream().findFirst();
            if (optionalTimeSlot.isPresent()) {
                TimeSlot timeSlot = optionalTimeSlot.get();
                model.get(timeSlot.getEnumerator(), timeSlot.getWeekday()).setTimeConstraint(tc);
//                model.put(timeSlot.getEnumerator(), timeSlot.getWeekday(), new PreferenceComponent(tc));
            }
        }

        weekCalendar.setWidth("80%");
        weekCalendar.getStyle().set("display", "grid");
        weekCalendar.getStyle().set("grid-template-columns", "repeat(8,auto)");
        weekCalendar.getStyle().set("grid-gap", "10px 10px");

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

        weekCalendar.add(timeslotLabel, mondayLabel, tuesdayLabel, wednesdayLabel,
                thursdayLabel, fridayLabel, saturdayLabel, sundayLabel, horizontalLine);

        //render timeslots

        for (int i = 1; i <= timeslotCount; i++) {
            Span tsLabel = new Span(Integer.toString(i));
            tsLabel.getStyle().set("grid-area", (i + 2) + " / 1 / span 1 / span 1");
            weekCalendar.add(tsLabel);
        }


        for (MultiKey<? extends Integer> entry : model.keySet()) { //model = timeslot x weekday --> assignmentcontainer
            PreferenceComponent preferenceComponent = model.get(entry);
            preferenceComponent.getStyle().set("grid-area", (entry.getKey(0) + 2) + " / " + (entry.getKey(1) + 1) + " /span 1 / span 1");
            weekCalendar.add(preferenceComponent);
        }

    }

    private VerticalLayout buildAddNewPreferenceComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setVisible(false);
        layout.add(new H2("Add new Preference:"));

        FormLayout formLayout = new FormLayout();


        ComboBox<DayOfWeek> selectWeekday = new ComboBox<>();
        selectWeekday.setRequired(true);
        selectWeekday.setRenderer(new ComponentRenderer<>(item -> {
            return new Span(item.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }));
        selectWeekday.setItems(DayOfWeek.values());
        selectWeekday.setValue(DayOfWeek.MONDAY);

        ComboBox<Integer> selectTimeslot = new ComboBox<>();
        selectTimeslot.setRequired(true);
        selectTimeslot.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        selectTimeslot.setValue(1);

        TextField preferenceName = new TextField();
        TextArea preferenceDescription = new TextArea();

        formLayout.addFormItem(selectWeekday, "Weekday");
        formLayout.addFormItem(selectTimeslot, "Timeslot");
        formLayout.addFormItem(preferenceName, "Preference Name");
        formLayout.addFormItem(preferenceDescription, "Description");


        HorizontalLayout buttonBar = new HorizontalLayout();
        Button save = new Button("Save", buttonClickEvent -> {
            //Create new TimeConstraint
            Dialog warning = ViewHelper.getConfirmationDialog("Do you really want to save this new preference?", confirmed -> {
                TimeConstraint timeConstraint = timeConstraintService.createTimeConstraint(selectTimeslot.getValue(), selectWeekday.getValue(),
                        preferenceName.getValue(), preferenceDescription.getValue());
                //Add to Current User
                userService.addTimeConstraint(userService.getSingleUser(SecurityUtils.getUsername()), timeConstraint);
            });
            warning.open();
        });
        Button reset = new Button("Reset", buttonClickEvent -> {
            selectWeekday.setValue(DayOfWeek.MONDAY);
            selectTimeslot.setValue(1);
            preferenceName.clear();
            preferenceDescription.clear();
        });
        Button cancel = new Button("Cancel", event -> {
            this.addNewPreferenceComponent.setVisible(false);
        });

        buttonBar.add(save, reset, cancel);

        layout.add(formLayout);
        layout.add(buttonBar);
        return layout;
    }

    static class PreferenceComponent extends VerticalLayout {
        private TimeConstraint timeConstraint;


        public PreferenceComponent() {
            buildComponent();
        }

        public PreferenceComponent(TimeConstraint timeConstraint) {
            this.timeConstraint = timeConstraint;
            buildComponent();
        }

        public void setTimeConstraint(TimeConstraint timeConstraint) {
            this.timeConstraint = timeConstraint;
            buildComponent();
        }

        private void buildComponent() {
            if (timeConstraint != null) {
                this.getStyle().remove("border");
                this.add(new Span(timeConstraint.getName()));
            } else {
                this.getStyle().set("border", "1px dotted");
            }


        }
    }

}
