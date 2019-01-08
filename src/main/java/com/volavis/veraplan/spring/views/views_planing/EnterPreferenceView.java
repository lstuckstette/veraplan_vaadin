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
import com.volavis.veraplan.spring.persistence.service.TimeConstraintService;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.components.ViewHelper;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

@PageTitle("Veraplan - Enter Preferences")
@Route(value = "planing/preference", layout = MainLayout.class)
public class EnterPreferenceView extends Div {

    private TimeConstraintService timeConstraintService;
    private UserService userService;
    private Div weekCalendar;
    private VerticalLayout addNewPreferenceComponent;

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

        contentLayout.add(weekCalendar);


        this.add(globalLayout);
    }

    private Div renderWeekCalendar() {
        Div calendar = new Div();
        MultiKeyMap<Integer, PreferenceComponent> model = new MultiKeyMap<>();
//        userService.get


        return calendar;
    }

    private VerticalLayout buildAddNewPreferenceComponent() {
        VerticalLayout layout = new VerticalLayout();
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

        public PreferenceComponent(TimeConstraint timeConstraint) {
            this.timeConstraint = timeConstraint;
            buildComponent();
        }

        private void buildComponent() {
            this.add(new Span(timeConstraint.getName()));

        }
    }

}
