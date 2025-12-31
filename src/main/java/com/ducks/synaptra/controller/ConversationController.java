package com.ducks.synaptra.controller;

import com.ducks.synaptra.conversation.ConversationService;
import com.ducks.synaptra.conversation.dto.MessageDTO;
import com.ducks.synaptra.log.LogTracer;
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

  @LogTracer(spanName = "conversation")
  @MessageMapping("/conversation")
  public void send(Message<MessageDTO> message, Principal principal) {
    conversationService.processMessage(message, principal.getName());
  }
}
