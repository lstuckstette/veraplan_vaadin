package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;

public class Footer extends Div {

    public Footer(){
        init();
    }

    private void init(){
        setClassName("w3-container w3-theme-d3 w3-padding-16 w3-bottom");
        H5 footerText = new H5();
        footerText.setText("Footer");
        add(footerText);
    }
}
