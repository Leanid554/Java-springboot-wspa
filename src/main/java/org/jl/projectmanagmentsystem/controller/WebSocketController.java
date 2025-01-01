package org.jl.projectmanagmentsystem.controller;

import org.jl.projectmanagmentsystem.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private static final Logger logger = LogManager.getLogger(WebSocketController.class);

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        logger.info("Received message: {}", message.getContent());
        return message; // Отправляет сообщение всем подписчикам на "/topic/messages"
    }
}
