package com.example.RabbitMQ.publisher;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.RabbitMQ.dto.User;

import ch.qos.logback.classic.Logger;

@Service
public class JsonPublisher {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.json}")
    private String routingJsonKey;

    private RabbitTemplate rabbitTemplate;

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(JsonPublisher.class);

    public JsonPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(User user) {
        LOGGER.info("Sending JSON message...");
        rabbitTemplate.convertAndSend(exchange, routingJsonKey, user);
    }

}
