package com.volavis.veraplan.spring.views.views_coredata;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;

@PageTitle("Veraplan - Enter Timeslots")
@Route(value = "coredata/timeslot", layout = MainLayout.class)
public class EnterTimeslotsView extends Div {

    public EnterTimeslotsView() {
        initView();
    }

    private void initView() {
        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        globalLayout.add(new H1("Enter Timeslots"));

        this.add(globalLayout);
    }
}
