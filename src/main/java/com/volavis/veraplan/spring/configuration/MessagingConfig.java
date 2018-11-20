package com.volavis.veraplan.spring.configuration;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean
    RabbitAdmin rabbitAdmin(RabbitTemplate template) {
        //TODO: use this to declare Queue/Exchanges during runtime!
        return new RabbitAdmin(template);
    }

    @Bean
    public TopicExchange incomingMessageExchange() {
        return new TopicExchange("message.incoming");
    }

    @Bean
    public FanoutExchange outgoingMessageExchange() {
        return new FanoutExchange("message.outgoing");
    }

    @Bean
    public Queue defaultQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue defaultQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding1(TopicExchange topic, Queue defaultQueue1) {
        return BindingBuilder.bind(defaultQueue1)
                .to(topic)
                .with("*.orange");
    }
}
