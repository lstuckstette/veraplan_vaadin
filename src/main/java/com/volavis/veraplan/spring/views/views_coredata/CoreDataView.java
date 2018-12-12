package com.volavis.veraplan.spring.views.views_coredata;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;

@PageTitle("Veraplan - Coredata")
@Route(value = "coredata", layout = MainLayout.class)
public class CoreDataView extends Div {

    public CoreDataView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        verticalLayout.add(new H1("Coredata"));

        this.add(verticalLayout);
    }
}
