package com.ducks.devutils.agent;

import com.ai.agentics.agent.Agent;
import com.ai.agentics.agent.AgentType;
import com.ai.agentics.agent.BaseAgent;
import com.ai.agentics.agent.ProviderConfig;
import com.ai.agentics.client.openai.data.Tool;
import com.ai.agentics.client.openai.data.ToolChoice;
import java.util.List;

public class PurchaseLogger extends BaseAgent {

  @Override
  public String identifier() {
    return "00d62c0b-430c-41ce-a819-cfb4703cef77";
  }

  @Override
  public String name() {
    return "PurchaseLogger";
  }

  @Override
  public String goal() {
    return "Purchase Activity Logging Agent";
  }

  @Override
  public AgentType agentType() {
    return AgentType.AGENT;
  }

  @Override
  public ProviderConfig providerConfig() {
    return new ProviderConfig("gpt-4o-mini", 0.2, 256, 1.0);
  }

  @Override
  public String prompt() {

    return """
          You are a $name

          $goal

          You must strictly follow the procedure defined by the available tool.
          All questions asked to the user must be derived exclusively from the tool parameters.

          Do not request information outside the defined procedure.
          The task is complete only when the purchase is successfully registered.
        """;
  }

  @Override
  public List<Tool> tools() {
    return super.tools();
  }

  @Override
  public List<Agent> agents() {
    return List.of();
  }

  @Override
  public ToolChoice toolChoice() {
    return ToolChoice.AUTO;
  }
}
