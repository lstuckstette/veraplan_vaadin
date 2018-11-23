package com.volavis.veraplan.spring.components;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.*;
import com.volavis.veraplan.spring.configuration.SecurityConfig;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;


@Tag("app-navigation")
@HtmlImport("components/app-navigation.html")
public class AppNavigation extends PolymerTemplate<AppNavigationModel> implements AfterNavigationObserver, BeforeEnterObserver {

    private static final Logger logger = LoggerFactory.getLogger(AppNavigation.class);

    @Id("menu-tabs")
    private Tabs tabs;


    @Id("accountsettings")
    private Div accountsettings;

    @Autowired
    public AppNavigation(UserService userService) {
        boolean loggedIn = SecurityUtils.isUserLoggedIn();
        this.getModel().setIsLoggedIn(loggedIn);
        if (loggedIn) {
            this.getModel().setUserName(userService.getFullName(SecurityUtils.getUsername()));
        }
    }

    public void setTabs(NavigationTab... navigationTabs) {
        tabs.removeAll();
        tabs.add(navigationTabs);
        tabs.addSelectedChangeListener(e -> navigateTo());
    }


    private void navigateTo() {
        if (tabs.getSelectedTab() instanceof NavigationTab) {
            NavigationTab selected = (NavigationTab) tabs.getSelectedTab();

            selected.getElement().setAttribute("selected", "");
            if (selected.getTarget() != null) {
                UI.getCurrent().navigate(selected.getTarget());
            }
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {


    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //set selected nav-tab after custom navigation:
        logger.info("current route: " + event.getNavigationTarget());
        tabs.getChildren().forEach(tab -> {
            if (tab instanceof NavigationTab) {

                if (((NavigationTab) tab).getTarget() != null) {
                    logger.info("tab: " + ((NavigationTab) tab).getTarget());
                    if (((NavigationTab) tab).getTarget().equals(event.getNavigationTarget())) {
                        logger.info("match!");
                        tab.getElement().setAttribute("selected", "");
                    }
                }
            }
        });

    }
}
