package com.volavis.veraplan.spring;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import com.volavis.veraplan.spring.components.AppNavigation;
import com.volavis.veraplan.spring.components.NavigationItemBuilder;
import com.volavis.veraplan.spring.components.NavigationTab;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.DashboardView;
import com.volavis.veraplan.spring.views.DrawingView;
import com.volavis.veraplan.spring.views.HelpView;
import com.volavis.veraplan.spring.views.UsersView;
import org.springframework.security.access.AccessDeniedException;

@BodySize()
@Tag("main-view")
@HtmlImport("main-view.html")
@Theme(value = Material.class, variant = Material.LIGHT)
public class LandingLayout extends PolymerTemplate<TemplateModel> implements RouterLayout, BeforeEnterObserver {

    @Id("app-navigation")
    private AppNavigation appNavigation;

    public LandingLayout() {
        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");

        //Fill NavigationBar:
        NavigationTab about = new NavigationItemBuilder().text("About").action("document.querySelector('login-view').$.aboutAnchor.scrollIntoView()").build();
        NavigationTab services = new NavigationItemBuilder().text("Services").action("document.querySelector('login-view').$.servicesAnchor.scrollIntoView()").build();
        NavigationTab contact = new NavigationItemBuilder().text("Contact").action("document.querySelector('login-view').$.contactAnchor.scrollIntoView()").build();

        appNavigation.setTabs(about, services, contact);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
            event.rerouteToError(AccessDeniedException.class);
        }
    }
}
