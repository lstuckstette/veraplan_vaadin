package com.volavis.veraplan.spring.views.components;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.*;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.templateModels.AppNavigationModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;


@Tag("app-navigation")
@HtmlImport("components/app-navigation.html")
public class AppNavigation extends PolymerTemplate<AppNavigationModel> implements BeforeEnterObserver {

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
        Arrays.stream(navigationTabs).forEach(tab -> {
            tab.getElement().addEventListener("click", domEvent -> {


                if (!tab.getSubmenu().isEmpty()) {
                    setSubMenu(tab.getSubmenu().stream().toArray(NavigationTab[]::new));
                } else {
                    removeSubMenu();
                }
                //deselect submenu 'selected'
                submenutabs.getChildren().forEach(subtab -> {
                    if (subtab instanceof NavigationTab) {
                        ((NavigationTab) subtab).setSelected(false);
                    }
                });

                menutabs.setSelectedTab(tab);
                if (tab.getTarget() != null) {
                    UI.getCurrent().navigate(tab.getTarget());
                }
            });
        });
    }

    public void addUserMenuTab(String text, String jsOnClick) {
        Button button = new Button(text);
        button.getElement().setAttribute("slot", "user-menu");
        button.getElement().setAttribute("onclick", jsOnClick);
        this.getElement().appendChild(button.getElement());
        this.getElement().appendChild(ElementFactory.createBr());
    }

    public void addUserMenuTab(String text, Class<? extends Component> target) {
        Button button = new Button(text, buttonClickEvent -> UI.getCurrent().navigate(target));
        button.getElement().setAttribute("slot", "user-menu");
        this.getElement().appendChild(button.getElement());
    }

    private void setSubMenu(NavigationTab... navigationTabs) {
        submenutabs.removeAll();
        submenutabs.add(navigationTabs);


        //add eventlistener with navigate action
        Arrays.stream(navigationTabs).forEach(tab -> {
            tab.getElement().addEventListener("click", domEvent -> {

                submenutabs.setSelectedTab(tab);
                if (tab.getTarget() != null) {
                    UI.getCurrent().navigate(tab.getTarget());
                }
            });
        });
//        submenutabs.getElement().removeAttribute("selected");
        submenu.getStyle().set("display", "flex");
    }

    private void removeSubMenu() {

        //deselect everything...
//        submenutabs.getChildren().forEach(tab -> {
//            if (tab instanceof NavigationTab) {
//                ((NavigationTab) tab).setSelected(false);
//            }
//        });
        submenutabs.removeAll();
        submenu.getStyle().set("display", "none");
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //set selected nav-tab after custom navigation:
        //logger.info("current route: " + event.getNavigationTarget());

//        selectTargetTab(event, menutabs);
//        selectTargetTab(event, submenutabs);


    }

    private void selectTargetTab(BeforeEnterEvent event, Tabs tabs) {
        tabs.getChildren().forEach(tab -> {
            if (tab instanceof NavigationTab) {

                if (((NavigationTab) tab).getTarget() != null) {
                    //logger.info("tab: " + ((NavigationTab) tab).getTarget());
                    if (((NavigationTab) tab).getTarget().equals(event.getNavigationTarget())) {
                        //logger.info("match!");
                        tabs.setSelectedTab((NavigationTab) tab);
                        //tab.getElement().setAttribute("selected", "");
                    }
                }
            }
        });
    }
}
