package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.volavis.veraplan.spring.MainLayout;

@PageTitle("Help")
@Route(value = "help", layout = MainLayout.class)
public class HelpView extends Div {

    public HelpView() {
        init();
        this.setClassName("content");
    }

    private void init() {

        for (int i = 0; i < 100; i++) {
            add(new Paragraph("derp"));
        }


    }
}
