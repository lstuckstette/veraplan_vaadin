package com.volavis.veraplan.spring.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.HighlightActions;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.volavis.veraplan.spring.views.DashboardView;
import com.volavis.veraplan.spring.views.HelpView;

public class NavBar extends Div {

    public NavBar(){
        init();
    }

    private void init(){
        setClassName("w3-top");
        add(buildNavbar());

    }

    private Div buildNavbar() {
        Div navbar = new Div();
        navbar.setClassName("w3-bar w3-theme-d2 w3-left-align w3-large");

        RouterLink home = new RouterLink("Logo", DashboardView.class);
        home.setHighlightCondition(HighlightConditions.sameLocation());
        home.setHighlightAction(HighlightActions.toggleClassName("w3-green"));
        home.setClassName("w3-bar-item w3-button w3-padding-large w3-theme-d4");
        Icon homeIcon = new Icon("vaadin", "home-o");
        //homeIcon.setClassName("fa fa-home w3-margin-right");
        home.add(homeIcon);

        RouterLink help = new RouterLink("", HelpView.class);
        help.setHighlightAction(HighlightActions.toggleClassName("w3-green"));
        help.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-hover-white");
        Icon helpIcon = new Icon("vaadin", "question-circle-o");
        //helpIcon.setClassName("fa fa-question-circle");
        help.add(helpIcon);

        Anchor example = new Anchor();
        example.setClassName("w3-bar-item w3-button w3-hide-small w3-padding-large w3-hover-white");
        example.setText("Example");

        Anchor account = new Anchor();
        account.setClassName("w3-bar-item w3-button w3-hide-small w3-right w3-padding-large w3-hover-white");
        Icon accountIcon = new Icon("vaadin", "user");
        //accountIcon.setClassName("fa fa-user");
        account.add(accountIcon);

        navbar.add(home, help, example, account);

        /*
        Div navbarWrapper = new Div();
        navbarWrapper.setClassName("w3-top");
        navbarWrapper.add(navbar);
        */

        return navbar;
    }
}
