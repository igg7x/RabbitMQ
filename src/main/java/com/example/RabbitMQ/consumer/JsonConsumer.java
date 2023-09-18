package com.example.RabbitMQ.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

@Service
public class JsonConsumer {

    private static final Logger logger = LoggerFactory.getLogger(JsonConsumer.class);

    @RabbitListener(queues = { "${rabbitmq.queue.json}" })
    public void consumeJSonMessage(User user) {
        logger.info(String.format("JSON", user.toString()));

    }
}
