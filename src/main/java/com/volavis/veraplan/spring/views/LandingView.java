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
import com.volavis.veraplan.spring.components.AppNavigation;
import com.volavis.veraplan.spring.components.NavigationItemBuilder;
import com.volavis.veraplan.spring.components.NavigationTab;

@PageTitle("LandingView")
@Tag("landing-view")
@HtmlImport("landing-view.html")
@BodySize()
@Route("landing")
@Theme(value = Material.class, variant = Material.LIGHT)
public class LandingView extends PolymerTemplate<TemplateModel> implements HasUrlParameter<String> {

    @Id("app-navigation")
    private AppNavigation appNavigation;

    public LandingView() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");

        NavigationTab about = new NavigationItemBuilder().text("About").action("document.querySelector('landing-view').$.aboutAnchor.scrollIntoView()").build();
        NavigationTab services = new NavigationItemBuilder().text("Services").action("document.querySelector('landing-view').$.servicesAnchor.scrollIntoView()").build();
        NavigationTab contact = new NavigationItemBuilder().text("Contact").action("document.querySelector('landing-view').$.contactAnchor.scrollIntoView()").build();
        appNavigation.setTabs(about, services, contact);

    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }

}
