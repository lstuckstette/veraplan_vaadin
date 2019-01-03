package com.volavis.veraplan.spring.views.components;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.volavis.veraplan.spring.persistence.entities.organisation.Assignment;
import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
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
        Calendar calendar = Calendar.getInstance();

        if (!assignment.getTimeSlots().isEmpty() && assignment.getTimeSlots().get(0).getDate() != null) {
            calendar.setTime(assignment.getTimeSlots().get(0).getDate());
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            switch (weekday) {
                case Calendar.MONDAY:
                    return 1;
                case Calendar.TUESDAY:
                    return 2;
                case Calendar.WEDNESDAY:
                    return 3;
                case Calendar.THURSDAY:
                    return 4;
                case Calendar.FRIDAY:
                    return 5;
                case Calendar.SATURDAY:
                    return 6;
                case Calendar.SUNDAY:
                    return 7;
                default:
                    return 0;
            }
        } else {
            return 0;
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
            indices.add(t.getEnumerator());
        }
        return indices;
    }


}
