package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.function.ValueProvider;
import com.volavis.veraplan.spring.persistence.service.EntityService;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManagerComponent<E, F extends Enum<F>> extends VerticalLayout {

    private EntityService<E, EntityFilter<F>> service;

    private Class<F> entityFields;

    private String headline;
    private String subHeadline;

    private Grid<E> entityGrid;
    private FormLayout editorFormLayout;
    private Binder<E> editorBinder;
    private Label infoLabel;

    public EntityManagerComponent(EntityService<E, EntityFilter<F>> service, Class<F> entityFields, String headline, String subHeadline) {

        super();
        this.headline = headline;
        this.subHeadline = subHeadline;
        this.service = service;

        this.entityFields = entityFields;


        // for editor:
        editorFormLayout = new FormLayout();
        editorBinder = new Binder<>();

        this.initView();

        //TODO: implement this - generalize ManageBuildings/ManageUsers !
    }

    private void initView() {

        this.setAlignItems(FlexComponent.Alignment.CENTER);
        //Headlines
        this.add(new H1(headline));
        this.add(new H2(subHeadline));

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
        Span addBuildingText = new Span("or ");
        Button addBuildingButton = new Button("Add New Entry");

        actionBarLayout.add(filterText, filtertype, filterInput, addBuildingText, addBuildingButton);
        this.add(actionBarLayout);

        //Main content
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setWidth("100%");
        //Grid:
        entityGrid = new Grid<>();


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

        entityGrid.setItemDetailsRenderer(new ComponentRenderer<>(this::getEntityEditorComponent));

        this.add(entityGrid);
        this.add(contentLayout);


        //Add-building pane(on button click)

        VerticalLayout addNewentityLayout = getAddEntityComponent();
        addNewentityLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        contentLayout.add(addNewentityLayout);

        addBuildingButton.addClickListener(event -> addNewentityLayout.setVisible(!addNewentityLayout.isVisible()));
    }

    public void addGridColumn(ValueProvider<E, ?> provider, String header) {
        entityGrid.addColumn(provider).setHeader(header);
    }

    public void addGridComponentColumn(ValueProvider<E, Component> valueProvider, String header) {
        entityGrid.addComponentColumn(valueProvider).setHeader(header);
    }

    public void addEditEntityTextField(String label, ValueProvider<E, String> valueProvider, Setter<E, String> valueSetter) {

        TextField newField = new TextField();
        editorFormLayout.addFormItem(newField, label);
        editorBinder.forField(newField)
                .withValidator(new StringLengthValidator(label + " can not be emtpy.", 1, null))
                .bind(valueProvider, valueSetter);

    }

    public void addEditEntityPasswordField(String label, ValueProvider<E, String> valueProvider, Setter<E, String> valueSetter) {

        PasswordField password = new PasswordField();
        password.setLabel("New Password");
        password.setEnabled(false);

        PasswordField retypePassword = new PasswordField();
        retypePassword.setLabel("Retype new Password");
        retypePassword.setEnabled(false);

        Checkbox alterPassword = new Checkbox("Change Password");

        SerializablePredicate<String> retypePasswordEqualsPassword = value ->
                retypePassword.getValue().equals(password.getValue());


        alterPassword.addValueChangeListener(state -> {
            password.setEnabled(state.getValue());
            retypePassword.setEnabled(state.getValue());

            if (state.getValue()) { //checkbox checked:
                editorBinder.forField(password)
                        .withValidator(new StringLengthValidator("Password can not be emtpy.", 1, null))
                        .withValidator(retypePasswordEqualsPassword, "Passwords do not match.")
                        .bind(valueProvider, valueSetter);
            } else {
                editorBinder.removeBinding(password);
            }
        });

        FormLayout.FormItem pfi = editorFormLayout.addFormItem(alterPassword, "Password");
        pfi.add(new HtmlComponent("br"));
        pfi.add(password);
        pfi.add(new HtmlComponent("br"));
        pfi.add(retypePassword);


    }

    public void addEditEntityCollection(String label, Collection<?> items, ValueProvider<E, Boolean> valueProvider, Setter<E, Boolean> valueSetter) {

        //TODO: this needs a rework! only for finite sets (e.x. roles ect...) how should the view know the entries? ~

        ListBox<Checkbox> collectionList = new ListBox<>();
        collectionList.setRenderer(new ComponentRenderer<>(cb -> cb)); //somehow this is needed...
        List<Checkbox> collectionItems = items.stream().map(item -> new Checkbox(item.toString())).collect(Collectors.toList());
        collectionList.setItems(collectionItems);

        editorFormLayout.addFormItem(collectionList, label);

        collectionItems.forEach(ci -> {
            editorBinder.forField(ci)
                    .bind(valueProvider, valueSetter);
        });

    }


    private VerticalLayout getEntityEditorComponent(E entity) {

        VerticalLayout alterEntityLayout = new VerticalLayout();
        alterEntityLayout.setAlignItems(FlexComponent.Alignment.CENTER);


        // FIELDS

        // CONTROLS
        infoLabel = new Label();
        Button save = new Button("Save");
        save.getElement().setAttribute("theme", "contained");
        Button reset = new Button("Reset");

        //layout.addFormItem(...)

        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.add(save, reset);

        // field binder

        // button action listener
        save.addClickListener(event -> {
            //show warning Dialog:
            Dialog warning;
            warning = ViewHelper.getConfirmationDialog("Do you really want to save entered changes? This can not be reversed!", evt -> {
                if (editorBinder.writeBeanIfValid(entity)) {

                    service.saveChanges(entity);

                    infoLabel.setText("Successfully saved changes.");
                } else {
                    String errorText = ViewHelper.getBinderErrorMessage(editorBinder);
                    infoLabel.setText("Error during save: " + errorText);
                }
            });

            warning.open();
        });

        reset.addClickListener(event -> {
            editorBinder.readBean(entity);
            infoLabel.setText("");
        });

        //load current user:
        editorBinder.readBean(entity);

        alterEntityLayout.add(editorFormLayout);
        alterEntityLayout.add(actions);

        return alterEntityLayout;
    }

    private VerticalLayout getAddEntityComponent() {

        VerticalLayout addEntityLayout = new VerticalLayout();
        addEntityLayout.setVisible(false);

        return addEntityLayout;
    }
}
