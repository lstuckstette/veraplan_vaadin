package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.templateModels.CollaborationToolkitModel;

@Tag("collaboration-toolkit")
@HtmlImport("components/collaboration-toolkit.html")
@JavaScript("js/webstomp.min.js")
@JavaScript("js/sockjs.min.js")
@JavaScript("js/paper-full.min.js")
public class CollaborationToolkit extends PolymerTemplate<CollaborationToolkitModel> implements HasComponents {

    public CollaborationToolkit(UserService userService, String channelID) {
        this.getModel().setUserName(userService.getFullName(SecurityUtils.getUsername()));
        this.getModel().setChannel(channelID);
    }
}
