package com.ducks.devutils.controller;

import com.ai.agentics.client.openai.data.*;
import com.ducks.devutils.conversation.ConversationService;
import com.ducks.devutils.conversation.dto.MessageDTO;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class ConversationController {

  private final ConversationService conversationService;

  @MessageMapping("/conversation")
  public void send(Message<MessageDTO> message, Principal principal) {
    conversationService.conversation(message, principal.getName());
  }
}
