package com.ducks.synaptra.agent;

import com.ducks.synaptra.client.openai.data.ToolChoice;
import java.util.List;

public class DockyardSupervisor extends BaseAgent {

  private final List<Agent> agentList;

  public DockyardSupervisor(List<Agent> agentList) {
    this.agentList = agentList;
    agentList.forEach(agent -> agent.setParent(this));
  }

  @Override
  public String identifier() {
    return "1151bb59-000b-44b2-ab8e-db1d41a6d3ee";
  }

  @Override
  public String name() {
    return "DockyardSupervisor";
  }

  @Override
  public String goal() {
    return "Agent responsible for orchestrating the execution of multiple agents, capable of selecting from the available options one or more agents to handle the user’s request.";
  }

  @Override
  public AgentType agentType() {
    return AgentType.SUPERVISOR;
  }

  @Override
  public ProviderConfig providerConfig() {
    return new ProviderConfig("gpt-4o-mini", 0.2, 256, 1.0);
  }

  @Override
  public String prompt() {
    return """
         You are the supervisor agent: $name

         Your objective: $goal

         From the available agents below, select one or more agents that are best suited to handle the user’s request.

         Available agents:
         #foreach($agent in $agents)
         - $agent.name(): $agent.goal()
         #end
        """;
  }

  @Override
  public List<Agent> agents() {
    return agentList;
  }

  @Override
  public ToolChoice toolChoice() {
    return ToolChoice.AUTO;
  }
}
