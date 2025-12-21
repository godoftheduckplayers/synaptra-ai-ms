package com.ducks.synaptraai.conversation.dto;

import java.time.LocalDateTime;

public record MessageDTO(
    String uuid, String content, MessageType type, MessageStatus status, LocalDateTime date) {}
