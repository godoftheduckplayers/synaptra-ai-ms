package com.ducks.synaptra.agent.health;

import com.ducks.synaptra.client.openai.data.Tool;
import com.ducks.synaptra.client.openai.data.ToolChoice;
import com.ducks.synaptra.model.agent.Agent;
import com.ducks.synaptra.model.agent.ProviderConfig;
import com.ducks.synaptra.model.tool.FinalizeRequestToolFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Factory responsible for building Agents for the Health Agentics system.
 *
 * <p>Primary use-case: build a lightweight Supervisor agent with a minimal procedural prompt,
 * optionally complemented with a catalog of child agents to guide routing decisions.
 */
public final class HealthAgentFactory {

  private HealthAgentFactory() {}

  /**
   * Builds the Supervisor agent for the Health system.
   *
   * <p>The supervisor prompt is intentionally short: it must only orchestrate by delegating work to
   * its child agents; as a fallback, it must state that it only handles the defined objective.
   */
  public static Agent buildSupervisor(
      ProviderConfig providerConfig, boolean supportsInterimMessages, List<Agent> agents) {
    String identifier = "supervisor.health";
    String name = "Health Supervisor";
    String goal = "Measure weight-loss progression and manage calorie control.";

    List<Agent> safeAgents = safeList(agents);

    String base =
        """
        You are the Supervisor of the Health Agentics system.

        Objective: %s

        Rules:
        - Act strictly as an orchestrator.
        - Select and invoke the most appropriate agent based on the user's request.
        - Do not perform domain actions yourself (no tracking, no calculations, no data collection).
        - Generate / produce responses in the same language in which the user is communicating.
        - If no agent is suitable, respond with:
          "I can only coordinate agents for the defined objective: %s"
        """
            .formatted(goal, goal)
            .trim();

    String catalog = buildAgentsCatalog(agents);

    String prompt = base + "\n\nAvailable agents:\n" + catalog.trim();

    Agent agent =
        buildAgent(
            identifier,
            name,
            goal,
            prompt,
            providerConfig,
            supportsInterimMessages,
            /* tools */ List.of(),
            /* parent */ null,
            /* agents */ safeAgents);

    FinalizeRequestToolFactory.finalizeRequestTool(agent);

    return agent;
  }

  public static Agent buildCalorieIntakeLoggerAgent(
      ProviderConfig providerConfig, Agent parentSupervisor) {
    String identifier = "agent.calorie-intake-logger";
    String name = "Calorie Intake Logger";
    String goal = "Collect calorie intake information and register it using the appropriate tool.";

    String prompt =
        """
        You are a data collection agent in a health tracking system.

        Your responsibility is to collect information incrementally and invoke the correct tool
        when enough data is available.

        Rules:
        - Collect or clarify ONE piece of information per message.
        - Never guess or infer missing values.
        - Do not provide advice, calculations, or summaries beyond what is required.
        - Use the tool schema as the source of truth for required data.
        - If required information is missing, ask a single clear question and wait.
        - Generate / produce responses in the same language in which the user is communicating.

        """
            .trim();

    Agent agent =
        buildAgent(
            identifier,
            name,
            goal,
            prompt,
            providerConfig,
            false,
            List.of(),
            parentSupervisor,
            List.of());
    CalorieIntakeToolFactory.registerCaloriesTool(agent);
    return agent;
  }

  public static Agent buildDailyCalorieConsumptionAgent(
      ProviderConfig providerConfig, Agent parentSupervisor) {

    String identifier = "agent.daily-calorie-consumption";
    String name = "Daily Calorie Consumption";
    String goal =
        "Identify the day requested by the user and retrieve the total calorie consumption for that specific date using the appropriate tool.";

    String prompt =
        """
            You are a health data query agent.

            Your responsibility is to identify which day the user wants to consult
            and invoke the appropriate tool to retrieve the calorie consumption for that date.

            Rules:
            - Never guess or infer missing values.
            - Do not provide advice, calculations, or summaries beyond what is required.
            - Use the tool schema as the source of truth for required data.
            - If required information is missing, ask a single clear question and wait.
            - Generate / produce responses in the same language in which the user is communicating.

            Após coletar o valor das calórias infome ao usuário o valor em kcal
            """
            .trim();

    Agent agent =
        buildAgent(
            identifier,
            name,
            goal,
            prompt,
            providerConfig,
            false,
            List.of(),
            parentSupervisor,
            List.of());

    DailyCalorieConsumptionToolFactory.registerDailyConsumptionTool(agent);

    return agent;
  }

  /** Generic agent builder used by the factory. */
  public static Agent buildAgent(
      String identifier,
      String name,
      String goal,
      String prompt,
      ProviderConfig providerConfig,
      boolean supportsInterimMessages,
      List<Tool> tools,
      Agent parent,
      List<Agent> agents) {
    Objects.requireNonNull(identifier, "identifier is required");
    Objects.requireNonNull(name, "name is required");
    Objects.requireNonNull(goal, "goal is required");
    Objects.requireNonNull(prompt, "prompt is required");
    Objects.requireNonNull(providerConfig, "providerConfig is required");

    return new Agent(
        identifier,
        name,
        goal,
        prompt,
        providerConfig,
        supportsInterimMessages,
        new ArrayList<>(safeList(tools)),
        ToolChoice.AUTO,
        parent,
        safeList(agents));
  }

  private static String buildAgentsCatalog(List<Agent> agents) {
    if (agents == null || agents.isEmpty()) return "";

    // Keep it capability-level: name + identifier + goal.
    return agents.stream()
        .map(
            a ->
                "- %s (%s): %s"
                    .formatted(
                        safeStr(a.getName()), safeStr(a.getIdentifier()), safeStr(a.getGoal())))
        .collect(Collectors.joining("\n"));
  }

  private static <T> List<T> safeList(List<T> list) {
    return list == null ? Collections.emptyList() : List.copyOf(list);
  }

  private static String safeStr(String s) {
    return s == null ? "" : s;
  }
}
