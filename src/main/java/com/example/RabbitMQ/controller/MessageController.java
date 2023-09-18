package com.example.RabbitMQ.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.RabbitMQ.dto.User;
import com.example.RabbitMQ.publisher.JsonPublisher;
import com.example.RabbitMQ.publisher.Publisher;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private Publisher publisher;
    private JsonPublisher jsonPublisher;

    public MessageController(Publisher publisher, JsonPublisher jsonPublisher) {
        this.publisher = publisher;
        this.jsonPublisher = jsonPublisher;
    }

    @GetMapping("/publish")
    public ResponseEntity<String> sennMessage(@RequestParam("message") String message) {
        publisher.sendMessage(message);
        return ResponseEntity.ok("Message sent successfully!");
    }

    @PostMapping("/publishJson")
    public ResponseEntity<String> sendJsonMessage(@RequestBody User user) {
        jsonPublisher.sendJsonMessage(user);
        return ResponseEntity.ok("Message  JSON sent successfully!");
    }

}
