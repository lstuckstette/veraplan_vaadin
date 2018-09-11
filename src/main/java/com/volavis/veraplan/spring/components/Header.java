package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;

public class Header extends Div {

    public Header(){
        init();
    }

    private void init(){
        setClassName("header");
        H1 h2Text = new H1("<Header>");
        Paragraph paragraph = new Paragraph("Header Subtext.");
        add(h2Text);
        add(paragraph);
    }
}
