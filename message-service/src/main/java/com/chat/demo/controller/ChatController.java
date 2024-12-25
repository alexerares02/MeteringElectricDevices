package com.chat.demo.controller;

import com.chat.demo.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sentToAdmin") // Handles messages from users
    public void sendMessage(ChatMessage message) {

        messagingTemplate.convertAndSendToUser("admin","/queue/messages", message);
    }

    @MessageMapping("/sentToUser/{userId}") // Handles messages from users
    public void sendMessage(ChatMessage message, @DestinationVariable String userId) {

        messagingTemplate.convertAndSendToUser(userId,"/queue/messages", message);
    }
}

