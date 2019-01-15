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
@HtmlImport("styles/shared-styles.html")
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
        submenu.getStyle().set("display", "none");
    }

    public void setMenuTabs(NavigationTab... navigationTabs) {

        Arrays.stream(navigationTabs).forEach(item ->item.addClassName("nav-tab"));

        menutabs.removeAll();
        menutabs.add(navigationTabs);
        Arrays.stream(navigationTabs).forEach(tab -> {
            tab.getElement().addEventListener("click", domEvent -> {

                //show submenu if not empty
                if (!tab.getSubmenu().isEmpty()) {
                    setSubMenu(tab.getSubmenu().stream().toArray(NavigationTab[]::new));
                    boolean visible = submenu.getStyle().get("display").equals("flex");

                    if (visible) {
                        //check toogle-off (if has same origin)
                        if (submenu.getElement().hasAttribute("origin") && submenu.getElement().getAttribute("origin").equals(tab.getLabel())) {
                            submenu.getStyle().set("display", "none");
                        } else {
                            //put origin
                            submenu.getElement().setAttribute("origin", tab.getLabel());
                        }
                    } else {
                        //show + put origin
                        submenu.getElement().setAttribute("origin", tab.getLabel());
                        submenu.getStyle().set("display", "flex");
                    }

                } else {
                    logger.info("remove submenu");
                    removeSubMenu();
                }
                //deselect submenu 'selected' TODO: doesnt do anything!
                submenutabs.getChildren().forEach(subtab -> {
                    if (subtab instanceof NavigationTab) {
                        ((NavigationTab) subtab).setSelected(false);
                    }
                });

                //navigate or toggle submenu
                menutabs.setSelectedTab(tab);
                if (tab.getTarget() != null) {
                    UI.getCurrent().navigate(tab.getTarget());
                }
            });
        });
    }

    private void setSubMenu(NavigationTab... navigationTabs) {
        submenutabs.removeAll();
        submenutabs.add(navigationTabs);

        //setComponent eventlistener with navigate action
        Arrays.stream(navigationTabs).forEach(tab -> {
            tab.getElement().addEventListener("click", domEvent -> {

                submenutabs.setSelectedTab(tab);
                if (tab.getTarget() != null) {
                    UI.getCurrent().navigate(tab.getTarget());
                }
            });
        });
    }

    private void removeSubMenu() {
        submenutabs.removeAll();
        submenu.getStyle().set("display", "none");
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

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

    }

}
