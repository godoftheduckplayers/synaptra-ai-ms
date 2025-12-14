package com.ducks.devutils.listener;

import com.ai.agentics.orchestration.event.agent.AgentExecutionListener;
import com.ai.agentics.orchestration.event.agent.contract.AgentResponseEvent;
import org.springframework.stereotype.Service;

@Service
public class AgentExecutionListenerImpl implements AgentExecutionListener {

  @Override
  public void onAgentResponseEvent(AgentResponseEvent agentResponseEvent) {
    System.out.println(agentResponseEvent);
  }
}
