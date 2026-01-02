package com.ducks.synaptra.agent;

import com.ducks.synaptra.client.openai.data.ToolChoice;
import java.util.List;

public class SynaptraSupervisor extends BaseAgent {

  private final List<Agent> agentList;

  public SynaptraSupervisor(List<Agent> agentList) {
    this.agentList = agentList;
    agentList.forEach(agent -> agent.setParent(this));
  }

  @Override
  public String getIdentifier() {
    return "1151bb59-000b-44b2-ab8e-db1d41a6d3ee";
  }

  @Override
  public String getName() {
    return "DockyardSupervisor";
  }

  @Override
  public String getGoal() {
    return "Agent responsible for orchestrating the execution of multiple agents, capable of selecting from the available options one or more agents to handle the user's request.";
  }

  @Override
  public boolean isSupportsInterimMessages() {
    return true;
  }

  @Override
  public AgentType getAgentType() {
    return AgentType.SUPERVISOR;
  }

  @Override
  public ProviderConfig getProviderConfig() {
    return new ProviderConfig("gpt-4o-mini", 0.2, 256, 1.0);
  }

  @Override
  public String getPrompt() {
    return """
         You are the supervisor agent: $name

         Your objective: $goal

         From the available agents below, select one or more agents that are best suited to handle the user's request.

         Available agents:
         #foreach($agent in $agents)
         - $agent.getName(): $agent.getGoal()
         #end

         #if($isSupportsInterimMessages)
          When using the routing function, generate a simple waiting message explaining what will be executed.
         #end
        """;
  }

  @Override
  public List<Agent> getAgents() {
    return agentList;
  }

  @Override
  public ToolChoice getToolChoice() {
    return ToolChoice.AUTO;
  }
}
