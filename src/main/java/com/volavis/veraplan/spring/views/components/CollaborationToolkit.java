package com.volavis.veraplan.spring.views.components;

import com.google.gson.Gson;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.templateModels.CollaborationToolkitModel;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

@Tag("collaboration-toolkit")
@HtmlImport("components/collaboration-toolkit.html")
@JavaScript("js/webstomp.min.js")
@JavaScript("js/sockjs.min.js")
@JavaScript("js/paper-full.min.js")
public class CollaborationToolkit extends PolymerTemplate<CollaborationToolkitModel> implements HasComponents {

    private static final Logger logger = LoggerFactory.getLogger(CollaborationToolkit.class);
    private ArrayList<AssignmentDragDropEventListener> listeners = new ArrayList<>();
    private Gson gson;

    public CollaborationToolkit(UserService userService, String channelID) {
        this.getModel().setUserName(userService.getFullName(SecurityUtils.getUsername()));
        this.getModel().setChannel(channelID);
        gson = new Gson();
    }

    @ClientCallable
    private void handleAssignmentDragDropEvent(String eventJSON) {
//        logger.info("RAW:" + eventJSON);
        notifyAssignmentDragDropEventListeners(gson.fromJson(eventJSON, AssignmentComponentMoveEvent.class));
    }

    public void sendDragDropEvent(AssignmentComponentMoveEvent event) {


        this.getModel().setDragDropEvent(gson.toJson(event));
    }

    public void addAssignmentDragDropEventListener(AssignmentDragDropEventListener listener) {
        listeners.add(listener);
    }

    public void removeAssignmentDragDropEventListener(AssignmentDragDropEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyAssignmentDragDropEventListeners(AssignmentComponentMoveEvent event) {
        listeners.forEach(listener -> listener.onDragDropEvent(event));
    }
}
