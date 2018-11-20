package com.volavis.veraplan.spring.messaging;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MessageSenderTest {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderTest.class);

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private TopicExchange topic;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        this.template.convertAndSend(topic.getName(),"key", "Hello World!");
        logger.info("sender-tick");
    }
}
