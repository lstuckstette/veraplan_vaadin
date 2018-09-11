package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;

public class Footer extends Div {

    public Footer(){
        init();
    }

    private void init(){
        setClassName("footer");
        H2 h2Text = new H2("<Footer>");
        add(h2Text);
    }
}
