package com.volavis.veraplan.spring.views.views_planing;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;

@PageTitle("Veraplan - Enter Preferences")
@Route(value = "planing/preference", layout = MainLayout.class)
public class EnterPreferenceView extends Div {

    public EnterPreferenceView() {
        initView();
    }

    private void initView() {
        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        globalLayout.add(new H1("Enter personal preference"));

        this.add(globalLayout);
    }
}
