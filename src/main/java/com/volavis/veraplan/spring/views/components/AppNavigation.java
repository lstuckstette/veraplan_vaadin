package com.volavis.veraplan.spring.views.components;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.*;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.templateModels.AppNavigationModel;
import org.springframework.beans.factory.annotation.Autowired;


@Tag("app-navigation")
@HtmlImport("components/app-navigation.html")
public class AppNavigation extends PolymerTemplate<AppNavigationModel> implements BeforeEnterObserver, AfterNavigationObserver {

    private static final Logger logger = LoggerFactory.getLogger(AppNavigation.class);

    @Id("menu-tabs")
    private Tabs menutabs;

    @Id("submenu-tabs")
    private Tabs submenutabs;

    @Id("sub-menu")
    private Div submenu;


    @Autowired
    public AppNavigation(UserService userService) {
        boolean loggedIn = SecurityUtils.isUserLoggedIn();
        this.getModel().setIsLoggedIn(loggedIn);
        if (loggedIn) {
            this.getModel().setUserName(userService.getFullName(SecurityUtils.getUsername()));
        }

    }

    public void setMenuTabs(NavigationTab... navigationTabs) {
        menutabs.removeAll();
        menutabs.add(navigationTabs);
        menutabs.addSelectedChangeListener(this::navigateTo);
    }

    public void addUserMenuTab(String text, String jsOnClick) {
        Button button = new Button(text);
        button.getElement().setAttribute("slot", "user-menu");
        button.getElement().setAttribute("onclick", jsOnClick);
        this.getElement().appendChild(button.getElement());
        this.getElement().appendChild(ElementFactory.createBr());
    }

    public void addUsermenuTab(String text, Class<? extends Component> target) {
        Button button = new Button(text, buttonClickEvent -> UI.getCurrent().navigate(target));
        button.getElement().setAttribute("slot", "user-menu");
        this.getElement().appendChild(button.getElement());
    }

    public void setSubMenu(NavigationTab... navigationTabs) {
        removeSubMenu();
        submenutabs.add(navigationTabs);
        submenutabs.addSelectedChangeListener(this::navigateTo);
        submenu.getStyle().set("display", "flex");
        //submenu.add(submenutabs);
    }

    private void removeSubMenu() {
        submenutabs.removeAll();
        submenu.getStyle().set("display", "none");
    }


    private void navigateTo(Tabs.SelectedChangeEvent event) {
        if (event.getSource().getSelectedTab() instanceof NavigationTab) {
            NavigationTab selected = (NavigationTab) event.getSource().getSelectedTab();
            selected.getElement().setAttribute("selected", "");
            if (selected.getTarget() != null) {

                if (!selected.getSubmenu().isEmpty()) {
                    setSubMenu(selected.getSubmenu().stream().toArray(NavigationTab[]::new));
                } else {
                    removeSubMenu();
                }

                UI.getCurrent().navigate(selected.getTarget());
            }
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //set selected nav-tab after custom navigation:
        //logger.info("current route: " + event.getNavigationTarget());
        menutabs.getChildren().forEach(tab -> {
            if (tab instanceof NavigationTab) {

                if (((NavigationTab) tab).getTarget() != null) {
                    //logger.info("tab: " + ((NavigationTab) tab).getTarget());
                    if (((NavigationTab) tab).getTarget().equals(event.getNavigationTarget())) {
                        //logger.info("match!");
                        menutabs.setSelectedTab((NavigationTab) tab);
                        //tab.getElement().setAttribute("selected", "");
                    }
                }
            }
        });

    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }
}
