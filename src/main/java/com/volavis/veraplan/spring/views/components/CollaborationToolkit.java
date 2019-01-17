package com.volavis.veraplan.spring.views.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.security.SecurityUtils;
import com.volavis.veraplan.spring.views.PlanCollaborationView;
import com.volavis.veraplan.spring.views.PlanView;
import com.volavis.veraplan.spring.views.templateModels.CollaborationToolkitModel;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
//        logger.info(eventJSON);

        SimpleModule simpleModule = new SimpleModule(eventJSON);
        simpleModule.addKeyDeserializer(MultiKey.class, new MultiKeyDeserializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(simpleModule);


        try {
            notifyAssignmentDragDropEventListeners(mapper.readValue(eventJSON, AssMoveEvent.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        notifyAssignmentDragDropEventListeners(gson.fromJson(eventJSON, AssMoveEvent.class));
    }

    public void sendDragDropEvent(AssMoveEvent event) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            this.getModel().setDragDropEvent(mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

//        this.getModel().setDragDropEvent(gson.toJson(event));
    }

    public void addAssignmentDragDropEventListener(AssignmentDragDropEventListener listener) {
        listeners.add(listener);
    }

    public void removeAssignmentDragDropEventListener(AssignmentDragDropEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyAssignmentDragDropEventListeners(AssMoveEvent event) {
        listeners.forEach(listener -> listener.onDragDropEvent(event));
    }


    static class MultiKeyDeserializer extends KeyDeserializer {

        @Override
        public Object deserializeKey(String key, DeserializationContext deserializationContext) throws IOException {
            if (key.matches("MultiKey\\[\\d+, \\d+\\]")) {
                String keys = key.replace("MultiKey", "");
                keys = keys.replaceAll("\\[", "");
                keys = keys.replaceAll("]", "");
                keys = keys.replaceAll(" ", "");
                String[] keyvals = keys.split(",");
                return new MultiKey<>(Integer.valueOf(keyvals[0]), Integer.valueOf(keyvals[1]));
            }

            return null;
        }
    }

}
