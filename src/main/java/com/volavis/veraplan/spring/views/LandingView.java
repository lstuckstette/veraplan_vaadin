package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.LandingLayout;

@PageTitle("LandingView")
@Tag("login-view")
@HtmlImport("views/login-view.html")
@Route(value = "landing", layout = LandingLayout.class)
public class LandingView extends PolymerTemplate<TemplateModel> {



    public LandingView() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");

            }


}
