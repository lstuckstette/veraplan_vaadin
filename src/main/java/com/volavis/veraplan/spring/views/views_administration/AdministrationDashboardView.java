package com.volavis.veraplan.spring.views.views_administration;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;

@PageTitle("Veraplan - Administration")
@Route(value = "administration", layout = MainLayout.class)
public class AdministrationDashboardView extends Div {

    public AdministrationDashboardView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        verticalLayout.add(new H1("Administration"));

        this.add(verticalLayout);

    }
}
