package com.ducks.synaptra.state;

import com.ducks.synaptra.event.agent.AgentExecutionListener;
import com.ducks.synaptra.event.agent.model.AgentResponseEvent;
import com.ducks.synaptra.model.agent.Agent;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class AgentStateService implements AgentExecutionListener {

  private static final Map<String, Agent> agentExecutionState = new HashMap<>();

  public Agent getCurrentAgent(String sessionUUID) {
    Agent currentAgent;
    if (CollectionUtils.isEmpty(agentExecutionState)
        || !agentExecutionState.containsKey(sessionUUID)) {
      // Executing the welcome message
      currentAgent = null;
    } else {
      currentAgent = agentExecutionState.get(sessionUUID);
    }
    return currentAgent;
  }

  @Override
  public void onAgentResponseEvent(AgentResponseEvent agentResponseEvent) {
    agentExecutionState.put(agentResponseEvent.getSessionId(), agentResponseEvent.getAgent());
  }
}
