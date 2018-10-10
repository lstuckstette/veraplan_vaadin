package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.volavis.veraplan.spring.MainLayout;

@PageTitle("Help")
@Route(value = "help", layout = MainLayout.class)
public class HelpView extends Div {

    public HelpView() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        init();
    }

    private void init() {

        for (int i = 0; i < 100; i++) {
            add(new Paragraph("derp "+ i));
        }


    }
}
