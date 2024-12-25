package com.chat.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String senderId;
    private String receiverId; // "admin" for admin messages
    private String content;

}
