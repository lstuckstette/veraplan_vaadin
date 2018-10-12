package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.security.SecurityUtils;


@Tag("app-navigation")
@HtmlImport("components/app-navigation.html")
public class AppNavigation extends PolymerTemplate<AppNavigationModel> implements AfterNavigationObserver {

    @Id("menu-tabs")
    private Tabs tabs;

    @Id("accountsettings")
    private Div accountsettings;

    public AppNavigation(){
        this.getModel().setIsLoggedIn(SecurityUtils.isUserLoggedIn()); //TODO: http://www.baasic.com/2014/11/28/Baasic-Polymer-demo-part-2/
    }

    public void setTabs(NavigationTab... navigationTabs) {
        tabs.add(navigationTabs);
        tabs.addSelectedChangeListener(e -> navigateTo());
    }

    public void showLoginButton() {
        Button loginButton = new Button();
        loginButton.setText("Login");
        loginButton.getElement().setAttribute("theme", "contained");
        loginButton.getElement().setAttribute("onclick", "document.querySelector('landing-view').$.loginDialog.open()");
        loginButton.getElement().setAttribute("slot", "loginButton");
        loginButton.getStyle().set("margin-left", "20px");
        this.getElement().appendChild(loginButton.getElement());
    }

    public void showRegisterButton() {
        Button registerButton = new Button();
        registerButton.setText("Register");
        registerButton.getElement().setAttribute("theme", "outlined");
        registerButton.getElement().setAttribute("onclick", "document.querySelector('landing-view').$.registerDialog.open()");
        registerButton.getElement().setAttribute("slot", "registerButton");
        registerButton.getStyle().set("margin-left", "20px");
        this.getElement().appendChild(registerButton.getElement());
    }

    public void showUserAccountMenu() {

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
}
