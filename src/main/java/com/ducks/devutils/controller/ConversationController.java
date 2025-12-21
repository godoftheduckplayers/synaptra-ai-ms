package com.ducks.devutils.controller;

import com.ai.agentics.client.openai.data.*;
import com.ai.agentics.prompt.UserInputPublisher;
import com.ducks.devutils.agent.DockyardSupervisor;
import com.ducks.devutils.agent.PurchaseLogger;
import com.ducks.devutils.conversation.dto.MessageDTO;
import com.ducks.devutils.conversation.dto.MessageStatus;
import com.ducks.devutils.conversation.dto.MessageType;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class ConversationController {

  private final SimpMessagingTemplate template;

  private final UserInputPublisher userInputPublisher;

  @GetMapping
  public Object test() {

    DockyardSupervisor dockyardSupervisor = new DockyardSupervisor(List.of(new PurchaseLogger()));

    userInputPublisher.publishEvent(
        UUID.randomUUID().toString(),
        dockyardSupervisor,
        "Quero registrar uma compra de um iphone 16 na data de 20/10/2025 no valor de 5k");

    return null;
  }

  @MessageMapping("/conversation")
  public void send(Message<MessageDTO> message, Principal principal) {
    template.convertAndSendToUser(
        principal.getName(),
        "/queue/conversation",
        new MessageDTO(
            UUID.randomUUID().toString(),
            "Olá, como esta?",
            MessageType.BOT,
            MessageStatus.FINISHED,
            LocalDateTime.now()));
  }
}
