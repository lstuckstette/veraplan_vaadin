package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Inline;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import com.volavis.veraplan.spring.LandingLayout;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.components.AppNavigation;
import com.volavis.veraplan.spring.components.NavigationItemBuilder;
import com.volavis.veraplan.spring.components.NavigationTab;

@PageTitle("LandingView")
@Tag("login-view")
@HtmlImport("views/login-view.html")
@Route(value = "landing", layout = LandingLayout.class)
public class LandingView extends PolymerTemplate<TemplateModel> implements HasUrlParameter<String> {



    public LandingView() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");

            }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }

}
