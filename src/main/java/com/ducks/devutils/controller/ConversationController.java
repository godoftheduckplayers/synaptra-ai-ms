package com.ducks.devutils.controller;

import com.ai.agentics.client.openai.data.*;
import com.ai.agentics.orchestration.event.agent.contract.AgentRequestEvent;
import com.ai.agentics.velocity.VelocityTemplateService;
import com.ducks.devutils.agent.DockyardSupervisor;
import com.ducks.devutils.agent.PurchaseLogger;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class ConversationController {

  private final ApplicationEventPublisher publisher;

  @GetMapping
  public Object test() {
    Message user = new Message("user", "Quero registrar uma nova compra", null, null, null);

    DockyardSupervisor dockyardSupervisor =
        new DockyardSupervisor(
            List.of(new PurchaseLogger()));

    publisher.publishEvent(
        new AgentRequestEvent(UUID.randomUUID().toString(), dockyardSupervisor, null, user));

    return null;
  }
}
