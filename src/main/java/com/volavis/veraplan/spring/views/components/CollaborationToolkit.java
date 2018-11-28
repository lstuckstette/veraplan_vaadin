package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.templateModels.DrawingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Tag("collaboration-toolkit")
@HtmlImport("components/collaboration-toolkit.html")
@JavaScript("js/webstomp.min.js")
@JavaScript("js/sockjs.min.js")
@JavaScript("js/paper-full.min.js")
public class CollaborationToolkit extends PolymerTemplate<DrawingViewModel> implements HasComponents {

    public CollaborationToolkit(UserService userService) {
        this.getModel().setUserName(userService.getFullName(SecurityUtils.getUsername()));
    }
}
