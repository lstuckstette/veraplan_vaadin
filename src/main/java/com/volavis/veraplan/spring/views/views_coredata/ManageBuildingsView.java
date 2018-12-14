package com.volavis.veraplan.spring.views.views_coredata;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
import com.volavis.veraplan.spring.persistence.service.BuildingService;
import com.volavis.veraplan.spring.views.components.BuildingField;
import com.volavis.veraplan.spring.views.components.ViewHelper;
import com.volavis.veraplan.spring.views.components.EntityFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;

@PageTitle("Veraplan - Manage Buildings")
@Route(value = "coredata/building", layout = MainLayout.class)
public class ManageBuildingsView extends Div {

    private BuildingService buildingService;

    @Autowired
    public ManageBuildingsView(BuildingService buildingService) {
        this.buildingService = buildingService;
        initView();
    }

    private void initView() {
        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        //Headlines
        globalLayout.add(new H1("Manage Buildings"));
        globalLayout.add(new H2("Alter existing buildings or add a new one"));

        //Action/Filter bar
        HorizontalLayout actionBarLayout = new HorizontalLayout();

        Span filterText = new Span("Filter by");
        ComboBox<BuildingField> filtertype = new ComboBox<>();
        TextField filterInput = new TextField();
        Span addBuildingText = new Span("or ");
        Button addBuildingButton = new Button("Add Building");

        actionBarLayout.add(filterText, filtertype, filterInput, addBuildingText, addBuildingButton);
        globalLayout.add(actionBarLayout);

        //Main content
        HorizontalLayout contentLayout = new HorizontalLayout();

        //Grid:
        Grid<Building> buildingGrid = new Grid<>(); //TODO width = 1px oO?

        //Dataprovider:
        CallbackDataProvider<Building, EntityFilter<BuildingField>> dataProvider = ViewHelper.getFilterDataProvider(buildingService);
        ConfigurableFilterDataProvider<Building, Void, EntityFilter<BuildingField>> filterWrapper =
                dataProvider.withConfigurableFilter();
        buildingGrid.setDataProvider(filterWrapper);

        filterInput.addValueChangeListener(event -> {
            String text = event.getValue();
            if (!text.trim().isEmpty() && text.trim().length() > 1) {
                filterWrapper.setFilter(new EntityFilter<>(text, filtertype.getValue()));
            } else {
                filterWrapper.setFilter(null);
            }
        });

        buildingGrid.addColumn(Building::getId).setHeader("Id");
        buildingGrid.addColumn(Building::getName).setHeader("Name");

        contentLayout.add(buildingGrid);
        globalLayout.add(contentLayout);


        //Add building pane(on button click)

        VerticalLayout addNewBuildingLayout = getAddBuildingComponent();

        contentLayout.add(addNewBuildingLayout);

        addBuildingButton.addClickListener(event -> addNewBuildingLayout.setVisible(!addNewBuildingLayout.isVisible()));


        this.add(globalLayout);
    }

    public VerticalLayout getAddBuildingComponent() {
        VerticalLayout addNewBuildingLayout = new VerticalLayout();
        addNewBuildingLayout.setVisible(false);
        addNewBuildingLayout.add(new H3("Add new Building"));
        addNewBuildingLayout.add(new H4("Enter information below:"));
        FormLayout newBuildingFormLayout = new FormLayout();

        TextField buildingName = new TextField();
        Label infoField = new Label();

        HorizontalLayout actionBar = new HorizontalLayout();
        Button save = new Button("Save");
        save.getElement().setAttribute("theme", "contained");
        Button reset = new Button("Reset");
        actionBar.add(save, reset);

        newBuildingFormLayout.addFormItem(buildingName, "Name");
        newBuildingFormLayout.add(infoField, actionBar);

        //Action Listener:
        save.addClickListener(event -> {

            //show warning Dialog:
            Dialog warning = ViewHelper.getConfirmationDialog("Do you really want to save this new Building?", evt -> {
                Building building = new Building();
                Binder<Building> binder = new Binder<>();
                binder.forField(buildingName)
                        .withValidator(new StringLengthValidator("Building name can not be empty.", 1, null))
                        .bind(Building::getName, Building::setName);
                if (binder.writeBeanIfValid(building)) {
                    buildingService.saveBuilding(building);
                    infoField.setText("Successfully saved new building.");
                } else {
                    String errorText = ViewHelper.getBinderErrorMessage(binder);
                    infoField.setText("Error during save: " + errorText);
                }

            });
            warning.open();
        });

        addNewBuildingLayout.add(newBuildingFormLayout);
        return addNewBuildingLayout;
    }

    public Component getBuildingEditor(Building building) {

        return null;
    }

}
