package com.volavis.veraplan.spring.websocket;


import com.google.gson.Gson;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import com.volavis.veraplan.spring.persistence.model.Channel;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.service.ChannelService;
import com.volavis.veraplan.spring.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;

import java.util.Map;


@Controller
public class WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    UserService userService;
    @Autowired
    ChannelService channelService;

    private Gson gson = new Gson();


    //TODO: implement chat-window
    //TODO: implement error handling: https://stackoverflow.com/questions/33741511/how-to-send-error-message-to-stomp-clients-with-spring-websocket
    @MessageMapping("/chat/{channelId}")
    @SendTo("/subscribe/chat/{channelId}")
    public String helloWorld(@Payload String message, @DestinationVariable String channelId, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("got CHAT on channel: " + channelId);
        User currentUser = userService.getByUsernameOrEmail(headerAccessor.getUser().getName());
        /*Channel currentChannel = channelService.getByName(channelId);
        //TODO: handle runtime Exception for above
        if (channelService.userInChannel(currentChannel, currentUser)) {
            return message;
        } else {
            return "{error: 403}";
        }*/
        return message;
    }


    //TODO: port to channel-based communication
    @MessageMapping("/drawing")
    @SendTo("/subscribe/drawing")
    public String handleDrawing(@Payload String drawing) {
        Map jsonMapping = gson.fromJson(drawing, Map.class);
        logger.info("got websocket-drawing from " + jsonMapping.get("owner"));
        //logger.info(jsonMapping.toString());
        return drawing;
    }


}
