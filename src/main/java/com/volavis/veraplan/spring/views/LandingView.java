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

@PageTitle("LandingView")
@Tag("landing-view")
@HtmlImport("landing-view.html")
@BodySize()
@Route("landing")
public class LandingView extends PolymerTemplate<TemplateModel> implements HasUrlParameter<String>, PageConfigurator {


   // @Id("menubar")
   // private Tabs menubar;

    public LandingView(){
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        //
//        RouterLink home = new RouterLink("Home",LandingView.class);
//        Tab homeTab = new Tab(home);
//        menubar.add(homeTab);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }

    @Override
    public void configurePage(InitialPageSettings settings) {


    }
}
