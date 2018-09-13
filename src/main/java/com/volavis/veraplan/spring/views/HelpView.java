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
        this.setClassName("w3-container w3-content");
        this.getStyle().set("max-width","1400px");
        this.getStyle().set("margin-top","80px");
        this.getStyle().set("margin-bottom","80px");
        init();
    }

    private void init() {

        for (int i = 0; i < 100; i++) {
            add(new Paragraph("derp "+ i));
        }


    }
}
