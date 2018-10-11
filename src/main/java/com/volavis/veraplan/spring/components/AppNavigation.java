package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;


@Tag("app-navigation")
@HtmlImport("components/app-navigation.html")
public class AppNavigation extends PolymerTemplate<TemplateModel> implements AfterNavigationObserver {

    @Id("menu-tabs")
    private Tabs tabs;

    public void setTabs(NavigationTab... navigationTabs) {
        tabs.add(navigationTabs);
        tabs.addSelectedChangeListener(e -> navigateTo());
    }

    private void navigateTo() {
        if (tabs.getSelectedTab() instanceof NavigationTab) {
            NavigationTab selected = (NavigationTab) tabs.getSelectedTab();

            selected.getElement().setAttribute("selected", "");
            if(selected.getTarget() != null){
                UI.getCurrent().navigate(selected.getTarget());
            }

        }


    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }
}
