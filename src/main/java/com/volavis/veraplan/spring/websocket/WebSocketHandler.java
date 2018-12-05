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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


@Controller
public class WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;

    private Gson gson = new Gson();


    @Autowired
    @Qualifier("clientOutboundChannel")
    private MessageChannel clientOutboundChannel;


    //HANDLE SUBSCRIPTIONS
    @SubscribeMapping("/subscribe/chat/{channelId}")
    public void handleChatSub(@DestinationVariable String channelId, SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = userService.getByUsernameOrEmail(Objects.requireNonNull(headerAccessor.getUser()).getName());
        if(!checkUserInChannel(headerAccessor,currentUser,channelId)){
            StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
            stompHeaderAccessor.setMessage("Unauthorized access to channel '" + channelId + "'.");
            stompHeaderAccessor.setSessionId(headerAccessor.getSessionId());
            this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders()));
        }
    }

    @SubscribeMapping("/subscribe/drawing/{channelId}")
    public void handleDrawingSub(@DestinationVariable String channelId, SimpMessageHeaderAccessor headerAccessor){
        User currentUser = userService.getByUsernameOrEmail(Objects.requireNonNull(headerAccessor.getUser()).getName());
        if(!checkUserInChannel(headerAccessor,currentUser,channelId)){
            StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
            stompHeaderAccessor.setMessage("Unauthorized access to channel '" + channelId + "'.");
            stompHeaderAccessor.setSessionId(headerAccessor.getSessionId());
            this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders()));
        }
    }

    //HANDLE MESSAGES:

    @MessageMapping("/chat/{channelId}")
    @SendTo("/subscribe/chat/{channelId}")
    public String handleChatMessage(@Payload String message, @DestinationVariable String channelId, SimpMessageHeaderAccessor headerAccessor) {

        Map jsonMapping = gson.fromJson(message, Map.class);

        User currentUser;
        //get User
        try {
            currentUser = userService.getByUsernameOrEmail(Objects.requireNonNull(headerAccessor.getUser()).getName());
        } catch (UsernameNotFoundException e){
            this.sendError(headerAccessor, "Unauthorized access to channel '" + channelId + "'.");
            return null;
        }

        String fullName = currentUser.getFirst_name() + " " + currentUser.getLast_name();
        jsonMapping.put("author", fullName);
        jsonMapping.put("timestamp", new SimpleDateFormat("HH:mm").format(new Date()));
        logger.info("got CHAT from " + fullName + " on channel " +channelId );

        if (checkUserInChannel(headerAccessor, currentUser, channelId)) {
            return new GsonBuilder().create().toJson(jsonMapping);
        } else {
            this.sendError(headerAccessor, "Unauthorized access to channel '" + channelId + "'.");
            return null;
        }

    }

    @MessageMapping("/drawing/{channelId}")
    @SendTo("/subscribe/drawing/{channelId}")
    public String handleDrawing(@Payload String drawing, @DestinationVariable String channelId, SimpMessageHeaderAccessor headerAccessor) {
        Map jsonMapping = gson.fromJson(drawing, Map.class);

        User currentUser;
        //get User
        try {
            currentUser = userService.getByUsernameOrEmail(Objects.requireNonNull(headerAccessor.getUser()).getName());
        } catch (UsernameNotFoundException e){
            this.sendError(headerAccessor, "Unauthorized access to channel '" + channelId + "'.");
            return null;
        }
        String fullName = currentUser.getFirst_name() + " " + currentUser.getLast_name();
        logger.info("got DRAW from " + fullName + " on channel " +channelId);

        //inject user/timestamp in message
        jsonMapping.put("author", fullName);
        jsonMapping.put("timestamp", new SimpleDateFormat("HH:mm").format(new Date()));

        if (checkUserInChannel(headerAccessor, currentUser, channelId)) {
            return new GsonBuilder().create().toJson(jsonMapping);
        } else {
            this.sendError(headerAccessor, "Unauthorized access to channel '" + channelId + "'.");
            return null;
        }
    }


    // HELPER METHODS

    private void sendError(SimpMessageHeaderAccessor headerAccessor, String errorText) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        stompHeaderAccessor.setMessage(errorText);
        stompHeaderAccessor.setSessionId(headerAccessor.getSessionId());
        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders()));
    }

    private boolean checkUserInChannel(SimpMessageHeaderAccessor headerAccessor, User user, String channelId) {
        try {
            return channelService.userInChannel(channelId, user);
        } catch (ChannelNotFoundException e) {
            return false;
        }
    }





}
