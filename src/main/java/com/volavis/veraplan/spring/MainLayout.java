/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.volavis.veraplan.spring;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.router.*;

import com.vaadin.flow.theme.NoTheme;
import com.volavis.veraplan.spring.components.Footer;
import com.volavis.veraplan.spring.components.Header;
import com.volavis.veraplan.spring.views.DashboardView;
import com.volavis.veraplan.spring.views.HelpView;


import java.util.HashMap;
import java.util.Map;

//@StyleSheet("frontend://styles/styles.css")
@NoTheme
@StyleSheet("https://www.w3schools.com/w3css/4/w3.css")
@StyleSheet("https://www.w3schools.com/lib/w3-theme-blue-grey.css")
@BodySize(height = "100vh", width = "100%")
public class MainLayout extends Div
        implements RouterLayout {

    private Map<String, RouterLink> targetPaths = new HashMap<>();

    /**
     * Constructor.
     */
    public MainLayout() {
        init();

    }

    private void init() {
        setClassName("container");
        getElement().getStyle().set("min-height", "100%");
        add(new Header());
        add(buildMenu());
        add(new Footer());
    }

    private Div buildMenu() {
        Div menuBar = new Div();
        menuBar.setClassName("menu");

        Icon homeicon = VaadinIcon.VAADIN_H.create();

        RouterLink home = new RouterLink("Home", DashboardView.class);
        RouterLink help = new RouterLink("Help", HelpView.class);
        //RouterLink login = new RouterLink("Login", LoginView.class);
        //login.getStyle().set("float","right");


        //home.setHighlightAction(HighlightActions.toggleClassName("active"));
        home.setHighlightCondition(HighlightConditions.sameLocation());
        home.setHighlightAction(HighlightActions.toggleClassName("active"));
        help.setHighlightAction(HighlightActions.toggleClassName("active"));


        targetPaths.put(home.getHref(), home);
        targetPaths.put(help.getHref(), help);


        HtmlContainer ul = new HtmlContainer("ul");
        ul.setClassName("topnav");
        ul.add(new ListItem(home), new ListItem(help));

        menuBar.add(ul);
        return menuBar;
    }
}
