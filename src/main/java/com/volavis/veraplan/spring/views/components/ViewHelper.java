package com.volavis.veraplan.spring.views.components;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.volavis.veraplan.spring.persistence.entities.ressources.Assignment;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.service.EntityService;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ViewHelper {

    private static final Logger logger = LoggerFactory.getLogger(ViewHelper.class);

    public static Dialog getConfirmationDialog(String warningText, ComponentEventListener<ClickEvent<Button>> confirmAction) {

        Dialog warning = new Dialog();
        warning.setCloseOnEsc(true);
        warning.setCloseOnOutsideClick(true);
        VerticalLayout warningLayout = new VerticalLayout();
        warningLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        warningLayout.add(new Span(warningText));
        Button confirm = new Button("Confirm", confirmAction);
        confirm.addClickListener(evt -> warning.close());
        Button cancel = new Button("Cancel", evt -> warning.close());
        HorizontalLayout buttonGroup = new HorizontalLayout(confirm, cancel);
        buttonGroup.setAlignItems(FlexComponent.Alignment.CENTER);
        warningLayout.add(buttonGroup);
        warning.add(warningLayout);
        return warning;

    }

    public static <B> String getBinderErrorMessage(Binder<B> binder) {
        BinderValidationStatus<B> validate = binder.validate();
        return validate.getFieldValidationStatuses()
                .stream().filter(BindingValidationStatus::isError)
                .map(BindingValidationStatus::getMessage)
                .map(Optional::get).distinct()
                .collect(Collectors.joining(", "));
    }

    public static <B, F extends EntityFilter> CallbackDataProvider<B, F> getFilterDataProvider(EntityService<B, F> service) {


        return DataProvider.fromFilteringCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    Optional<F> filter = query.getFilter();

                    if (filter.isPresent()) {
                        return service.getAllInRange(filter.get(), offset, limit);
                    } else {
                        return service.getAllInRange(offset, limit);
                    }
                },
                query -> {
                    int count;
                    if (query.getFilter().isPresent()) {
                        count = service.countAll(query.getFilter().get());
                        logger.info("got count: " + count);
                    } else {
                        count = service.countAll();
                        logger.info("got count: " + count);
                    }

                    return count;
                }
        );
    }

    public static int getAssignmentDayOfWeek(Assignment assignment) {

        if (assignment.getTimeSlots().isEmpty()) {
            return 0;
        } else {
            TimeSlot timeSlot = assignment.getTimeSlots().get(0);
            return timeSlot.getWeekday();
        }
    }

    public static void setupWeekCalendar(Div grid, int timeslotCount) {
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

        grid.add(timeslotLabel, mondayLabel, tuesdayLabel, wednesdayLabel,
                thursdayLabel, fridayLabel, saturdayLabel, sundayLabel, horizontalLine);

        //render timeslots

        for (int i = 1; i <= timeslotCount; i++) {
            Span tsLabel = new Span(Integer.toString(i));
            tsLabel.getStyle().set("grid-area", (i + 2) + " / 1 / span 1 / span 1");
            grid.add(tsLabel);
        }
    }

    public static int getAssignmentTimeSlotSmallestEnumerator(Assignment assignment) {
        List<Integer> enumerators = getAssignmentTimeSlotEnumerators(assignment);

        if (!enumerators.isEmpty()) {
            enumerators.sort(Comparator.naturalOrder());
            return enumerators.get(0);
        } else {
            return 0;
        }
    }

    public static List<Integer> getAssignmentTimeSlotEnumerators(Assignment assignment) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (TimeSlot t : assignment.getTimeSlots()) {
            indices.add(t.getTimeSlotIndex());
        }
        return indices;
    }

    public static FlowTable generateWeekCalendar() {
        FlowTable flowTable = new FlowTable(6, 7);
        flowTable.add(1, 1, new Span("Stunde"));
        flowTable.add(2, 1, new Span("Montag"));
        flowTable.add(3, 1, new Span("Dienstag"));
        flowTable.add(4, 1, new Span("Mittwoch"));
        flowTable.add(5, 1, new Span("Donnerstag"));
        flowTable.add(6, 1, new Span("Freitag"));
        //TimeSlots
        for (int ts = 2; ts <= 7; ts++) {
            flowTable.add(1, ts, new Span("" + (ts - 1)));
        }

        return flowTable;
    }
}
