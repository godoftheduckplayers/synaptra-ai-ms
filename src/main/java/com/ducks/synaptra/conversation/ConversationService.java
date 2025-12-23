package com.ducks.synaptra.conversation;

import com.ducks.synaptra.agent.Agent;
import com.ducks.synaptra.agent.DockyardSupervisor;
import com.ducks.synaptra.agent.PurchaseLogger;
import com.ducks.synaptra.conversation.dto.MessageDTO;
import com.ducks.synaptra.conversation.dto.MessageStatus;
import com.ducks.synaptra.conversation.dto.MessageType;
import com.ducks.synaptra.orchestration.event.agent.AgentExecutionListener;
import com.ducks.synaptra.orchestration.event.agent.contract.AgentResponseEvent;
import com.ducks.synaptra.orchestration.event.answer.AnswerExecutionListener;
import com.ducks.synaptra.orchestration.event.answer.contract.AnswerResponseEvent;
import com.ducks.synaptra.prompt.UserInputPublisher;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class ConversationService implements AnswerExecutionListener, AgentExecutionListener {

  private static final Map<String, Agent> agentExecutionState = new HashMap<>();

  private final SimpMessagingTemplate template;
  private final UserInputPublisher userInputPublisher;

  public void conversation(Message<MessageDTO> message, String sessionUUID) {

    Agent currentAgent = null;
    if (CollectionUtils.isEmpty(agentExecutionState)) {
      currentAgent = new DockyardSupervisor(List.of(new PurchaseLogger()));
    } else {
      currentAgent = agentExecutionState.get(sessionUUID);
    }

    userInputPublisher.publishEvent(sessionUUID, currentAgent, message.getPayload().content());
  }

  @Override
  public void onAnswerExecutionResponseEvent(AnswerResponseEvent answerResponseEvent) {
    template.convertAndSendToUser(
        answerResponseEvent.sessionId(),
        "/queue/conversation",
        new MessageDTO(
            UUID.randomUUID().toString(),
            answerResponseEvent.response(),
            MessageType.BOT,
            MessageStatus.FINISHED,
            LocalDateTime.now()));
  }

  @Override
  public void onAgentResponseEvent(AgentResponseEvent agentResponseEvent) {
    agentExecutionState.put(agentResponseEvent.sessionId(), agentResponseEvent.agent());
  }
}
