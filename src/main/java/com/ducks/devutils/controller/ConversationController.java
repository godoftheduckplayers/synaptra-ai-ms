package com.ducks.devutils.controller;

import com.ai.agentics.client.openai.data.*;
import com.ai.agentics.prompt.UserInputPublisher;
import com.ducks.devutils.agent.DockyardSupervisor;
import com.ducks.devutils.agent.PurchaseLogger;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class ConversationController {

  private final UserInputPublisher userInputPublisher;

  @GetMapping
  public Object test() {

    DockyardSupervisor dockyardSupervisor = new DockyardSupervisor(List.of(new PurchaseLogger()));

    userInputPublisher.publishEvent(
        UUID.randomUUID().toString(), dockyardSupervisor, "Quero registrar uma compra de um iphone 16 na data de 20/10/2025 no valor de 5k");

    return null;
  }
}
