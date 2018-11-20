package com.volavis.veraplan.spring.messaging;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "#{defaultQueue1.name}")
public class MessageReceiverTest {

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverTest.class);

    @RabbitHandler
    public void receive(String message) {
        logger.info("Got Message: '" + message + "' !");
    }
}
