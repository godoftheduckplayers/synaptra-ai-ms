package com.ducks.synaptra.conversation;

import com.ducks.synaptra.agent.money.FinanceAgentBootstrapService;
import com.ducks.synaptra.conversation.dto.MessageDTO;
import com.ducks.synaptra.conversation.dto.MessageStatus;
import com.ducks.synaptra.conversation.dto.MessageType;
import com.ducks.synaptra.event.UserInputMessagePublisher;
import com.ducks.synaptra.event.answer.AnswerExecutionListener;
import com.ducks.synaptra.event.answer.model.AnswerRequestEvent;
import com.ducks.synaptra.log.LogTracer;
import com.ducks.synaptra.model.agent.Agent;
import com.ducks.synaptra.model.agent.ProviderConfig;
import com.ducks.synaptra.state.AgentStateService;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConversationService implements AnswerExecutionListener {

  private final SimpMessagingTemplate template;
  private final UserInputMessagePublisher userInputMessagePublisher;
  private final AgentStateService agentExecutionState;
  private final Agent supervisorParent;

  public ConversationService(
      SimpMessagingTemplate template,
      UserInputMessagePublisher userInputMessagePublisher,
      AgentStateService agentExecutionState,
      FinanceAgentBootstrapService financeAgentBootstrapService) {
    this.template = template;
    this.userInputMessagePublisher = userInputMessagePublisher;
    this.agentExecutionState = agentExecutionState;
    ProviderConfig providerConfig = new ProviderConfig("gpt-4o-mini", 0.2, 256, 1.0);
    supervisorParent = financeAgentBootstrapService.buildFinanceSupervisor(providerConfig);
  }

  @LogTracer(spanName = "process_message")
  public void processMessage(Message<MessageDTO> message, String sessionUUID) {

    Agent currentAgent = agentExecutionState.getCurrentAgent(sessionUUID);
    if (currentAgent == null) {
      template.convertAndSendToUser(
          sessionUUID,
          "/queue/conversation",
          new MessageDTO(
              UUID.randomUUID().toString(),
              "Olá, bem-vindo a Synaptra",
              MessageType.BOT,
              MessageStatus.FINISHED,
              LocalDateTime.now()));
      currentAgent = supervisorParent;
    }
    userInputMessagePublisher.publisherUserMessage(
        sessionUUID, currentAgent, message.getPayload().content());
  }

  @Override
  public void onAnswerExecutionResponseEvent(AnswerRequestEvent answerRequestEvent) {
    template.convertAndSendToUser(
        answerRequestEvent.getSessionId(),
        "/queue/conversation",
        new MessageDTO(
            UUID.randomUUID().toString(),
            answerRequestEvent.getResponse(),
            MessageType.BOT,
            MessageStatus.FINISHED,
            LocalDateTime.now()));
  }
}
