package com.volavis.veraplan.spring.websocket;


import com.google.gson.Gson;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import com.volavis.veraplan.spring.persistence.model.Channel;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.service.ChannelService;
import com.volavis.veraplan.spring.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

import java.util.Map;


@Controller
public class WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    UserService userService;
    @Autowired
    ChannelService channelService;

    @Autowired
    @Qualifier("clientOutboundChannel")  //TODO: does this work oO?!
    private MessageChannel clientOutboundChannel;

    private Gson gson = new Gson();

    //HANDLE SUBSCRIPTIONS

    @SubscribeMapping("/subscribe/chat/{channelID}")
    public void handleChatSub(@DestinationVariable String channelID, SimpMessageHeaderAccessor headerAccessor) {

        //TODO: WIP!!!
        //https://stackoverflow.com/questions/39641477/send-stomp-error-from-spring-websocket-program
        //StompCommand.ERROR leads to disconnect of the client....
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        stompHeaderAccessor.setMessage("errorMessage~");
        stompHeaderAccessor.setSessionId(headerAccessor.getSessionId());
        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], stompHeaderAccessor.getMessageHeaders()));

    }


    //TODO: unnecessary?
    @MessageExceptionHandler
    @SendToUser("/error")
    public String handleChatSubException(Throwable exception) {

        return exception.getMessage();

    }


    //HANDLE MESSAGES:

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
