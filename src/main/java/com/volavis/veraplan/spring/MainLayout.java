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

import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

import com.vaadin.flow.router.*;

import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import com.volavis.veraplan.spring.views.*;
import com.volavis.veraplan.spring.views.components.AppNavigation;
import com.volavis.veraplan.spring.views.components.NavigationItemBuilder;

import com.volavis.veraplan.spring.views.components.NavigationTab;
import com.volavis.veraplan.spring.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;


@BodySize()
@Tag("main-view")
@HtmlImport("main-view.html")
@Theme(value = Material.class, variant = Material.LIGHT)
public class MainLayout extends PolymerTemplate<TemplateModel> implements RouterLayout, BeforeEnterObserver {

    @Id("app-navigation")
    private AppNavigation appNavigation;

    public MainLayout() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");

        //Fill NavigationBar:
        NavigationTab help = new NavigationItemBuilder().text("Help").target(HelpView.class).build();
        NavigationTab users = new NavigationItemBuilder().text("Users").target(UsersView.class).build();
        NavigationTab drawing = new NavigationItemBuilder().text("Drawing").target(DrawingView.class).build();

        NavigationTab dash = new NavigationItemBuilder().text("Dashboard").target(DashboardView.class).build();

        appNavigation.setMenuTabs(dash, help, users, drawing);

        //Fill User-Menu (right side)
        appNavigation.addUsermenuTab("Profile", ProfileView.class);
        appNavigation.addUserMenuTab("Mesages", "");
        appNavigation.addUserMenuTab("Settings", "");
        appNavigation.addUsermenuTab("Help", HelpView.class);
        appNavigation.addUserMenuTab("Log Out", "document.querySelector('main-view').$.logoutDialog.open()");

        //NavigationTab sub1 = new NavigationItemBuilder().text("sub1").build();
        //NavigationTab sub2 = new NavigationItemBuilder().text("sub2").build();
        //appNavigation.setSubMenu(sub1, sub2);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
            event.rerouteToError(AccessDeniedException.class);
        }
    }
}
