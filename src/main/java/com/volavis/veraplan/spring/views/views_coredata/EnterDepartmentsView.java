package com.volavis.veraplan.spring.views.views_coredata;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;

@PageTitle("Veraplan - Enter Departments")
@Route(value = "coredata/department", layout = MainLayout.class)
public class EnterDepartmentsView extends Div {

    public EnterDepartmentsView() {
        initView();
    }

    private void initView() {
        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        globalLayout.add(new H1("Enter Departments"));

        this.add(globalLayout);
    }
}
