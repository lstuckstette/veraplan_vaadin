package com.volavis.veraplan.spring.views.views_coredata;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
import com.volavis.veraplan.spring.persistence.service.BuildingService;
import com.volavis.veraplan.spring.views.components.*;
import org.springframework.beans.factory.annotation.Autowired;


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
        EntityManagerComponentBuilder<Building, BuildingField> emcb = new EntityManagerComponentBuilder<>(buildingService, BuildingField.class);
        emcb = emcb.setHeadline("Manage Buildings").setSubHeadline("Browse all buildings or filter the list. Clicking on an item will show an editor window.")
                .addGridColumn(Building::getId, "Id")
                .addGridColumn(Building::getShortName, "Short Name")
                .addGridColumn(Building::getName, "Name")
                .setAddEntityComponent(this.getAddBuildingComponent())
                .setEditEntityRenderer(this::getBuildingEditor);

        this.add(emcb.buildComponent());
    }

    private VerticalLayout getAddBuildingComponent() {
        VerticalLayout addNewBuildingLayout = new VerticalLayout();

        addNewBuildingLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        addNewBuildingLayout.setVisible(false);
        addNewBuildingLayout.add(new H3("Add new Building"));
        addNewBuildingLayout.add(new H4("Enter information below:"));
        FormLayout newBuildingFormLayout = new FormLayout();

        //Attribute fields:
        TextField buildingName = new TextField();
        TextField buildingShortName = new TextField();


        newBuildingFormLayout.addFormItem(buildingName, "Name");
        newBuildingFormLayout.addFormItem(buildingShortName, "Short Name");

        Label infoField = new Label();
        newBuildingFormLayout.add(infoField);

        HorizontalLayout actionBar = new HorizontalLayout();
        actionBar.setAlignItems(FlexComponent.Alignment.CENTER);
        Button save = new Button("Save");
        save.getElement().

                setAttribute("theme", "contained");

        Button reset = new Button("Reset");
        actionBar.add(save, reset);


        newBuildingFormLayout.add(infoField);

        //Action Listener:
        save.addClickListener(event ->

        {

            //show warning Dialog:
            Dialog warning = ViewHelper.getConfirmationDialog("Do you really want to save this new Building?", evt -> {
                Building building = new Building();
                Binder<Building> binder = new Binder<>();
                binder.forField(buildingName)
                        .withValidator(new StringLengthValidator("Building name can not be empty.", 1, null))
                        .bind(Building::getName, Building::setName);

                binder.forField(buildingShortName)
                        .withValidator(new StringLengthValidator("Building short name can not be empty.", 1, null))
                        .bind(Building::getShortName, Building::setShortName);

                if (binder.writeBeanIfValid(building)) {
                    //save
                    buildingService.saveBuilding(building);
                    Notification notification = new Notification();
                    notification.setText("Successfully saved new building.");
                    notification.open();

                } else {
                    String errorText = ViewHelper.getBinderErrorMessage(binder);

                    Notification notification = new Notification();
                    notification.setText("Error during save: " + errorText);
                    notification.open();

//                    infoField.setText("Error during save: " + errorText);
                }

            });
            warning.open();
        });

        addNewBuildingLayout.add(newBuildingFormLayout);
        addNewBuildingLayout.add(actionBar);
        return addNewBuildingLayout;
    }

    private Component getBuildingEditor(Building building) {

        return new VerticalLayout();
    }

}
