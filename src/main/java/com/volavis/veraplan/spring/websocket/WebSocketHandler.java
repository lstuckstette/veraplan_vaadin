package com.volavis.veraplan.spring.websocket;


import com.google.gson.Gson;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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
    SimpMessagingTemplate messagingTemplate;

    private Gson gson = new Gson();

    @MessageMapping("/hello")
    @SendTo("/subscribe/hello")
    public String helloWorld(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("got WS: " + message);
        return "Hey there  " + headerAccessor.getSessionId() + " !";
    }


    @MessageMapping("/drawing")
    @SendTo("/subscribe/drawing")
    public String handleDrawing(@Payload String drawing) {
        //TODO: on-connect assign random color
        logger.info("got WS-path");
        logger.info(gson.fromJson(drawing, Map.class).toString());
        return drawing;
    }


}
