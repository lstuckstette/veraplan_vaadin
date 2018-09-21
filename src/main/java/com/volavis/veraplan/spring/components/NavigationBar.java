package com.volavis.veraplan.spring.components;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.HighlightActions;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.security.CurrentUser;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.DashboardView;
import com.volavis.veraplan.spring.views.HelpView;
import com.volavis.veraplan.spring.views.LoginView;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("main-nav")
@HtmlImport("components/main-nav.html")
public class NavigationBar extends PolymerTemplate<TemplateModel> {

    private static final Logger logger = LoggerFactory.getLogger(NavigationBar.class);

    @Id("nav-items")
    private Div navitems;

    public void addNavItem(Component navItem) {
        navitems.add(navItem);
    }

    @EventHandler
    private void handleRegister() {
        Notification.show("Register clicked!");
        logger.info("Register clicked!");
    }

    private void initNonAuth() {
        RouterLink home = new RouterLink("Home", LoginView.class);
        home.setHighlightCondition(HighlightConditions.sameLocation());
        home.setHighlightAction(HighlightActions.toggleClassName("w3-white"));
        home.setClassName("w3-bar-item w3-button w3-padding-large w3-white");

        Anchor about = new Anchor();
        about.setText("About");
        about.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-hover-white");

        Anchor register = new Anchor();
        register.setText("Register");
        register.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-hover-white");

        Anchor login = new Anchor();
        login.setText("Login");
        login.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-hover-white w3-right");

        navitems.add(home, about, register, login);
    }
}
