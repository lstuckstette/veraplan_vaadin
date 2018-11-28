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


        //Submenus
        NavigationTab administration_manageusers = new NavigationItemBuilder().text("Manage users").target(null).build();
        NavigationTab administration_manageressources = new NavigationItemBuilder().text("Manage users").target(null).build(); //TODO: do this for all entity-groups?
        NavigationTab administration_exportplan = new NavigationItemBuilder().text("Export plan").target(null).build();
        NavigationTab administration_generateplan = new NavigationItemBuilder().text("Generate plan").target(null).build();

        NavigationTab coredata_enterbuilding = new NavigationItemBuilder().text("Enter buildings").target(null).build();
        NavigationTab coredata_enterroom = new NavigationItemBuilder().text("Enter rooms").target(null).build();
        NavigationTab coredata_enterdepartment = new NavigationItemBuilder().text("Enter departments").target(null).build();
        NavigationTab coredata_entertimeslots = new NavigationItemBuilder().text("Enter timeslots").target(null).build();

        NavigationTab planing_preference = new NavigationItemBuilder().text("Enter personal preferences").target(null).build();
        NavigationTab planing_assignment = new NavigationItemBuilder().text("Enter ressource assignment").target(null).build();

        //Mainmenu
        NavigationTab dashboard = new NavigationItemBuilder().text("Dashboard").target(DashboardView.class).build();

        NavigationTab administration = new NavigationItemBuilder().text("Administration").target(AdministrationView.class)
                .submenu(administration_manageusers, administration_manageressources,
                        administration_exportplan, administration_generateplan).build();

        NavigationTab coredata = new NavigationItemBuilder().text("Core data").target(CoreDataView.class)
                .submenu(coredata_enterbuilding, coredata_enterroom,
                        coredata_enterdepartment, coredata_entertimeslots).build();

        NavigationTab planing = new NavigationItemBuilder().text("Planing").target(PlaningView.class)
                .submenu(planing_preference, planing_assignment).build();

        NavigationTab plan = new NavigationItemBuilder().text("View Plans").target(PlanView.class).build();

        //Demo NavigationBar:
        NavigationTab help = new NavigationItemBuilder().text("Help").target(HelpView.class).build();
        NavigationTab users = new NavigationItemBuilder().text("Users").target(UsersView.class).build();
        NavigationTab drawing = new NavigationItemBuilder().text("Drawing").target(DrawingView.class).build();


        appNavigation.setMenuTabs(dashboard, administration, coredata, planing, plan, drawing);

        //Fill User-Menu (right side)
        appNavigation.addUsermenuTab("Profile", ProfileView.class);
        appNavigation.addUserMenuTab("Mesages", "");
        appNavigation.addUserMenuTab("Settings", "");
        appNavigation.addUsermenuTab("Help", HelpView.class);
        appNavigation.addUserMenuTab("Log Out", "document.querySelector('main-view').$.logoutDialog.open()");

    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
            event.rerouteToError(AccessDeniedException.class);
        }
    }
}
