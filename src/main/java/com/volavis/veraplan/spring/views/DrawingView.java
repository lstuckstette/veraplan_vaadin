package com.volavis.veraplan.spring.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.PolymerTemplateModel.DrawingViewModel;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("drawing-view")
@HtmlImport("views/drawing-view.html")
@JavaScript("js/webstomp.min.js")
@JavaScript("js/sockjs.min.js")
@JavaScript("js/paper-full.min.js")
@Route(value = "drawing", layout = MainLayout.class)
public class DrawingView extends PolymerTemplate<DrawingViewModel> {

    @Autowired
    public DrawingView(UserService userService) {
        this.getModel().setUserName(userService.getFullName(SecurityUtils.getUsername()));
        init();
    }

    private void init() {

    }
}
