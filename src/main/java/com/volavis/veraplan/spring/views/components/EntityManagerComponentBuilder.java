package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.volavis.veraplan.spring.persistence.service.EntityService;

import javax.validation.constraints.NotNull;
import java.util.EnumSet;


public class EntityManagerComponentBuilder<E, F extends Enum<F>> {

    private EntityService<E, EntityFilter<F>> service;

    private Class<F> entityFields;

    private Grid<E> entityGrid;

    private String headline = "HEADLINE";
    private String subHeadline = "SUBHEADLINE";

    private boolean hasAddNewEntityComponent = false;
    private Component addEntityComponent;

    private boolean hasEditEntityComponent = false;
    private Renderer<E> editEntityRenderer;

    public EntityManagerComponentBuilder(EntityService<E, EntityFilter<F>> service, Class<F> entityFields) {
        this.service = service;
        this.entityFields = entityFields;

        entityGrid = new Grid<>();
    }

    public EntityManagerComponentBuilder<E, F> setHeadline(String headline) {
        this.headline = headline;
        return this;
    }

    public EntityManagerComponentBuilder<E, F> setSubHeadline(String subHeadline) {
        this.subHeadline = subHeadline;
        return this;
    }

    public EntityManagerComponentBuilder<E, F> addGridColumn(ValueProvider<E, ?> provider, String header) {
        entityGrid.addColumn(provider).setHeader(header);
        return this;
    }

    public EntityManagerComponentBuilder<E, F> addGridComponentColumn(ValueProvider<E, Component> valueProvider, String header) {
        entityGrid.addComponentColumn(valueProvider).setHeader(header);
        return this;
    }

    public EntityManagerComponentBuilder<E, F> setEditEntityRenderer(@NotNull Renderer<E> editEntityRenderer) {
        this.editEntityRenderer = editEntityRenderer;
        this.hasEditEntityComponent = true;
        return this;
    }

    public EntityManagerComponentBuilder<E, F> setAddEntityComponent(@NotNull Component addEntityComponent) {
        this.hasAddNewEntityComponent = true;
        this.addEntityComponent = addEntityComponent;
        this.addEntityComponent.setVisible(false);
        return this;
    }

    public VerticalLayout buildComponent() {//TODO

        VerticalLayout globalLayout = new VerticalLayout();

        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        //Headlines
        globalLayout.add(new H1(this.headline));
        globalLayout.add(new H2(this.subHeadline));

        //Action/Filter bar
        HorizontalLayout actionBarLayout = new HorizontalLayout();
        actionBarLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Span filterText = new Span("Filter by");

        ComboBox<F> filtertype = new ComboBox<>();
        filtertype.setRequired(true);
        filtertype.setItemLabelGenerator(Enum::toString);
        EnumSet<F> entityFieldsSet = EnumSet.allOf(entityFields);
        filtertype.setItems(entityFieldsSet);
        filtertype.setValue(entityFieldsSet.stream().findFirst().orElse(null));

        TextField filterInput = new TextField();
        filterInput.setValueChangeMode(ValueChangeMode.EAGER);

        Span addEntityText = new Span("or ");
        Button addEntityButton = new Button("Add New Entry");

        if (this.hasAddNewEntityComponent) {
            actionBarLayout.add(filterText, filtertype, filterInput, addEntityText, addEntityButton);
        } else {
            actionBarLayout.add(filterText, filtertype, filterInput);
        }

        globalLayout.add(actionBarLayout);

        //Main content
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setWidth("100%");

        //Dataprovider:
        CallbackDataProvider<E, EntityFilter<F>> dataProvider = ViewHelper.getFilterDataProvider(service);
        ConfigurableFilterDataProvider<E, Void, EntityFilter<F>> filterWrapper =
                dataProvider.withConfigurableFilter();
        entityGrid.setDataProvider(filterWrapper);

        filterInput.addValueChangeListener(event -> {
            String text = event.getValue();
            if (!text.trim().isEmpty() && text.trim().length() > 1) {
                filterWrapper.setFilter(new EntityFilter<>(text, filtertype.getValue()));
            } else {
                filterWrapper.setFilter(null);
            }
        });

        if (this.hasEditEntityComponent) {
            entityGrid.setItemDetailsRenderer(this.editEntityRenderer);
        }

        globalLayout.add(entityGrid);
        globalLayout.add(contentLayout);


        //Add-entity pane(on button click)
        if (this.hasAddNewEntityComponent) {
            contentLayout.add(this.addEntityComponent);
            addEntityButton.addClickListener(event -> this.addEntityComponent.setVisible(!this.addEntityComponent.isVisible()));
        }


        return globalLayout;
    }

    //
//@SuppressWarnings("varargs")
//public <T, C extends Component & HasValue<?, T>> EntityManagerComponentBuilder addEditEntityBoundFormItem(String label, C component, ValueProvider<E, T> valueProvider, Setter<E, T> valueSetter, PredicateValidator<T>... validators) {
//editEntityFormItems.add(component);
//
//Binder.BindingBuilder<E, T> bindingBuilder = editEntityBinder.forField(component);
//
//for (PredicateValidator<T> v : validators) {
//bindingBuilder = bindingBuilder.withValidator(v.getPredicate(), v.getError());
//}
//
//bindingBuilder.bind(valueProvider, valueSetter);
//return this;
//}
//
//public static class PredicateValidator<P> {
//private SerializablePredicate<P> predicate;
//private String error;
//
//public PredicateValidator(SerializablePredicate<P> predicate, String error) {
//this.predicate = predicate;
//this.error = error;
//}
//
//public SerializablePredicate<P> getPredicate() {
//return predicate;
//}
//
//public String getError() {
//return error;
//}
//}
//
}
