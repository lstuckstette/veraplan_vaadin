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

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;

import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;

import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;

import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.components.NavigationBar;
import com.volavis.veraplan.spring.components.NavigationItemBuilder;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.DashboardView;
import com.volavis.veraplan.spring.views.HelpView;

//@StyleSheet("frontend://styles/styles.css")
//@NoTheme
@StyleSheet("https://www.w3schools.com/w3css/4/w3.css")
@StyleSheet("https://www.w3schools.com/lib/w3-theme-blue-grey.css")
@Tag("main-view")
@HtmlImport("views/main-view.html")
public class MainLayout extends PolymerTemplate<TemplateModel> implements RouterLayout {

    @Id("main-nav")
    NavigationBar navbar;

    public MainLayout() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");

        navbar.addNavItem(new NavigationItemBuilder().linkText("Home").targetClass(DashboardView.class).backgroundColor("w3-white").build());
        navbar.addNavItem(new NavigationItemBuilder().linkText("Help").targetClass(HelpView.class).build());
        //Add Nav-Items depending on Access-Level


    }

    private void init() {
       //Build Navigation:

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
