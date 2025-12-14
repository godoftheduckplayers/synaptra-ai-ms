package com.ducks.devutils.listener;

import com.ai.agentics.orchestration.event.answer.AnswerExecutionListener;
import com.ai.agentics.orchestration.event.answer.contract.AnswerResponseEvent;
import org.springframework.stereotype.Service;

@Service
public class AnswerExecutionListenerImpl implements AnswerExecutionListener {

  @Override
  public void onAnswerExecutionResponseEvent(AnswerResponseEvent answerResponseEvent) {
    System.out.println(answerResponseEvent);
  }
}
