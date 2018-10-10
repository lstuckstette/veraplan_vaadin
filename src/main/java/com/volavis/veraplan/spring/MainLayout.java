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


import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;

import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import com.volavis.veraplan.spring.components.NavigationItemBuilder;

import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.DashboardView;
import com.volavis.veraplan.spring.views.HelpView;
import org.springframework.security.access.AccessDeniedException;


@StyleSheet("https://www.w3schools.com/w3css/4/w3.css")
@StyleSheet("https://www.w3schools.com/lib/w3-theme-blue-grey.css")
@Tag("main-view")
@HtmlImport("main-view.html")
@Theme(value= Material.class, variant = Material.LIGHT)
public class MainLayout extends PolymerTemplate<TemplateModel> implements RouterLayout, BeforeEnterObserver {

    @Id("menu-items")
    private Tabs navbar;

    public MainLayout() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        
        navbar.add(new NavigationItemBuilder().linkText("Home").targetClass(DashboardView.class).build());
        navbar.add(new NavigationItemBuilder().linkText("Help").targetClass(HelpView.class).build());
        //Add Nav-Items depending on Access-Level

        //TODO: Rework Navigation: no router links... see BakeryApp/AppNavigation & BakeryApp/MainView
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!SecurityUtils.isAccessGranted(event.getNavigationTarget())){
            event.rerouteToError(AccessDeniedException.class);
        }
    }
}
