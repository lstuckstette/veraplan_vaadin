package com.volavis.veraplan.spring.websocket;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.exception.ChannelNotFoundException;
import com.volavis.veraplan.spring.persistence.service.ChannelService;
import com.volavis.veraplan.spring.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Controller
public class WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    UserService userService;
    @Autowired
    ChannelService channelService;

    private Gson gson = new Gson();


    @Autowired
    @Qualifier("clientOutboundChannel")
    private MessageChannel clientOutboundChannel;


    //HANDLE SUBSCRIPTIONS

    @SubscribeMapping("/subscribe/chat/{channelId}")
    public void handleChatSub(@DestinationVariable String channelId, SimpMessageHeaderAccessor headerAccessor) {

        User currentUser = userService.getByUsernameOrEmail(headerAccessor.getUser().getName());
        String fullName = currentUser.getFirst_name() + " " + currentUser.getLast_name();
        try {
            if (!channelService.userInChannel(channelId, currentUser)) {
                StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                stompHeaderAccessor.setMessage("Unauthorized access to channel '" + channelId + "'.");
                stompHeaderAccessor.setSessionId(headerAccessor.getSessionId());
                this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders()));
            } else {
                logger.info("User '" + fullName + "' successfully subbed to chat-channel '" + channelId + "'.");
            }
        } catch (ChannelNotFoundException e) {
            StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
            stompHeaderAccessor.setMessage(e.getMessage());
            stompHeaderAccessor.setSessionId(headerAccessor.getSessionId());
            this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders()));
        }

    }


    //HANDLE MESSAGES:

    @MessageMapping("/chat/{channelId}")
    @SendTo("/subscribe/chat/{channelId}")
    public String handleChatMessage(@Payload String message, @DestinationVariable String channelId, SimpMessageHeaderAccessor headerAccessor) {

        Map jsonMapping = gson.fromJson(message, Map.class);
        User currentUser = userService.getByUsernameOrEmail(headerAccessor.getUser().getName());

        //Set username:
        String fullName = currentUser.getFirst_name() + " " + currentUser.getLast_name();
        jsonMapping.put("author", fullName);
        jsonMapping.put("timestamp", new SimpleDateFormat("HH:mm").format(new Date()));
        logger.info("got CHAT on channel: " + channelId + " by User: " + fullName);

        try {
            if (channelService.userInChannel(channelId, currentUser)) {
                return new GsonBuilder().create().toJson(jsonMapping);
            } else {
                this.sendError(headerAccessor, "Unauthorized access to channel '" + channelId + "'.");
                return null;
            }
        } catch (ChannelNotFoundException e) {
            this.sendError(headerAccessor, e.getMessage());
            return null;
        }
    }

    private void sendError(SimpMessageHeaderAccessor headerAccessor, String errorText) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        stompHeaderAccessor.setMessage(errorText);
        stompHeaderAccessor.setSessionId(headerAccessor.getSessionId());
        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders()));
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
