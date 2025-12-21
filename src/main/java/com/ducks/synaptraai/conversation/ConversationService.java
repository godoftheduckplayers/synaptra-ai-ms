package com.ducks.synaptraai.conversation;

import com.ai.agentics.orchestration.event.answer.AnswerExecutionListener;
import com.ai.agentics.orchestration.event.answer.contract.AnswerResponseEvent;
import com.ai.agentics.prompt.UserInputPublisher;
import com.ducks.synaptraai.agent.DockyardSupervisor;
import com.ducks.synaptraai.agent.PurchaseLogger;
import com.ducks.synaptraai.conversation.dto.MessageDTO;
import com.ducks.synaptraai.conversation.dto.MessageStatus;
import com.ducks.synaptraai.conversation.dto.MessageType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConversationService implements AnswerExecutionListener {

  private final SimpMessagingTemplate template;
  private final UserInputPublisher userInputPublisher;

  public void conversation(Message<MessageDTO> message, String sessionUUID) {

    DockyardSupervisor dockyardSupervisor = new DockyardSupervisor(List.of(new PurchaseLogger()));

    userInputPublisher.publishEvent(
        sessionUUID, dockyardSupervisor, message.getPayload().content());
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
}
