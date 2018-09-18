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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;

import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;

import com.vaadin.flow.router.*;

import com.volavis.veraplan.spring.components.NavBar;
import com.volavis.veraplan.spring.views.DashboardView;
import com.volavis.veraplan.spring.views.HelpView;

import java.util.Objects;

//@StyleSheet("frontend://styles/styles.css")
//@NoTheme
@StyleSheet("https://www.w3schools.com/w3css/4/w3.css")
@StyleSheet("https://www.w3schools.com/lib/w3-theme-blue-grey.css")
//@StyleSheet("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css")
@BodySize(height = "100%", width = "100%")
public class MainLayout extends Div implements RouterLayout {

    //private Div contentContainer;

    /**
     * Constructor.
     */
    public MainLayout() {
        init();
    }

    private void init() {
        this.setClassName("pagecontent");


        //change body class
        UI.getCurrent().getElement().getClassList().add("w3-theme-l5");

        //this.contentContainer = new Div();
        //contentContainer.setClassName("w3-container w3-content");

        add(buildNavbar());
        //(this.contentContainer);
        //add(buildFooter());


    }
    /*
    @Override
    public void showRouterLayoutContent(HasElement content) {
        if (content != null) {
            this.contentContainer.removeAll();
            this.contentContainer.add(Objects.requireNonNull((Component) content));
        }
    }
        */

    private Div buildFooter() {
        Div footer = new Div();
        footer.setClassName("w3-container w3-theme-d3 w3-padding-16 w3-bottom");
        footer.getStyle().set("position", "absolute");
        footer.getStyle().set("bottom", "0");
        H5 footerText = new H5();
        footerText.setText("Footer");
        footer.add(footerText);
        return footer;
    }

    private Div buildNavbar() {
        Div navbar = new Div();
        navbar.setClassName("w3-bar w3-theme-d2 w3-left-align w3-large");

        RouterLink home = new RouterLink("Logo", DashboardView.class);
        home.setHighlightCondition(HighlightConditions.sameLocation());
        home.setHighlightAction(HighlightActions.toggleClassName("w3-green"));
        home.setClassName("w3-bar-item w3-button w3-padding-large w3-theme-d4");
        Icon homeIcon = new Icon("vaadin", "home-o");
        //homeIcon.setClassName("fa fa-home w3-margin-right");
        home.add(homeIcon);

        RouterLink help = new RouterLink("", HelpView.class);
        help.setHighlightAction(HighlightActions.toggleClassName("w3-green"));
        help.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-hover-white");
        Icon helpIcon = new Icon("vaadin", "question-circle-o");
        //helpIcon.setClassName("fa fa-question-circle");
        help.add(helpIcon);

        Anchor example = new Anchor();
        example.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-hover-white");
        example.setText("Example");

        Anchor account = new Anchor();
        account.setClassName("w3-bar-item w3-button w3-hide-small w3-right w3-padding-large w3-hover-white");
        Icon accountIcon = new Icon("vaadin", "user");
        //accountIcon.setClassName("fa fa-user");
        account.add(accountIcon);

        navbar.add(home, help, example, account);

        Div navbarWrapper = new Div();
        navbarWrapper.setClassName("w3-top");
        navbarWrapper.add(navbar);

        return navbarWrapper;
    }

}
