package com.ducks.synaptra.agent.money;

import com.ducks.synaptra.agent.AgentSpec;
import com.ducks.synaptra.agent.SupervisorSpec;
import com.ducks.synaptra.agent.factory.AgentFactory;
import com.ducks.synaptra.agent.factory.SupervisorPromptFactory;
import com.ducks.synaptra.client.openai.data.Tool;
import com.ducks.synaptra.client.openai.data.ToolChoice;
import com.ducks.synaptra.model.agent.Agent;
import com.ducks.synaptra.model.agent.ProviderConfig;
import com.ducks.synaptra.tool.factory.ChitChatToolFactory;
import com.ducks.synaptra.tool.factory.FallbackToolFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * Builds a Finance supervisor and child agents: - Purchase Recorder (register_purchase) - Purchases
 * Lister (list_purchases)
 */
@Service
public class FinanceAgentBootstrapService {

  public Agent buildFinanceSupervisor(ProviderConfig providerConfig) {
    Objects.requireNonNull(providerConfig, "providerConfig is required");

    // 1) Child agents
    Agent purchaseRecorder = buildPurchaseRecorder(providerConfig);
    Agent purchaseLister = buildPurchaseLister(providerConfig);

    List<Agent> children = List.of(purchaseRecorder, purchaseLister);

    // 2) Supervisor prompt (minimal)
    SupervisorSpec supervisorSpec =
        new SupervisorSpec(
            "supervisor.finance",
            "Finance Supervisor",
            "Coordinate personal finance tasks: record purchases and list purchases.",
            providerConfig,
            true,
            ToolChoice.AUTO,
            children,
            true, // enableRoutingTool
            true, // enableChitChatTool
            true, // enableFallbackTool
            true, // enableFinalizeTool (se você tiver finalize no supervisor)
            "pt-BR",
            "concise",
            "When the user intent is unclear (record vs list), ask ONE clarification question and wait.",
            List.of() // extraSupervisorTools (opcional)
            );

    String supervisorPrompt = SupervisorPromptFactory.buildPrompt(supervisorSpec, children);

    Agent supervisor =
        new Agent(
            supervisorSpec.identifier(),
            supervisorSpec.name(),
            supervisorSpec.goal(),
            supervisorPrompt,
            supervisorSpec.providerConfig(),
            supervisorSpec.supportsInterimMessages(),
            null,
            supervisorSpec.toolChoice(),
            null,
            children);

    // 3) Supervisor tools (chit-chat + fallback)
    ChitChatToolFactory.registerChitChatTool(supervisor);

    // Complementa o fallback com as tools "não sistêmicas" dos agentes filhos
    List<Tool> childNonSystemTools = collectTools(purchaseRecorder, purchaseLister);
    FallbackToolFactory.registerFallbackTool(supervisor, childNonSystemTools);

    // Se você quiser também fallback/chitchat nos agentes filhos, pode registrar neles também
    // (opcional)
    // ChitChatToolFactory.registerChitChatTool(purchaseRecorder);
    // ChitChatToolFactory.registerChitChatTool(purchaseLister);

    return supervisor;
  }

  private Agent buildPurchaseRecorder(ProviderConfig providerConfig) {
    String prompt =
        """
                You are a finance data-collection agent.

                Objective: Collect purchase details and record them using the appropriate tool.

                Rules:
                - Ask for ONE missing piece of information per message.
                - Never guess missing values.
                - When all required fields are provided, call the tool to record the purchase.
                - Respond in the same language as the user.
                """;

    AgentSpec spec =
        new AgentSpec(
            "agent.finance.purchase-recorder",
            "Purchase Recorder",
            "Collect purchase details and record them.",
            prompt,
            providerConfig,
            false,
            ToolChoice.AUTO,
            null,
            List.of(),
            false,
            List.of() // customTools
            );

    Agent agent = AgentFactory.build(spec); // :contentReference[oaicite:2]{index=2}

    // Tool(s) do agente
    RegisterPurchaseToolFactory.registerRegisterPurchaseTool(agent);

    return agent;
  }

  private Agent buildPurchaseLister(ProviderConfig providerConfig) {
    String prompt =
        """
                You are a finance retrieval agent.

                Objective: Retrieve and list recorded purchases for the requested period.

                Rules:
                - If dates/period are missing or ambiguous, ask ONE clarification question.
                - Never invent purchases.
                - When enough info is provided, call the listing tool.
                - Respond in the same language as the user.
                """;

    AgentSpec spec =
        new AgentSpec(
            "agent.finance.purchase-lister",
            "Purchases Lister",
            "List recorded purchases based on a requested period.",
            prompt,
            providerConfig,
            false,
            ToolChoice.AUTO,
            null,
            List.of(),
            false,
            List.of());

    Agent agent = AgentFactory.build(spec); // :contentReference[oaicite:3]{index=3}

    // Tool(s) do agente
    ListPurchasesToolFactory.registerListPurchasesTool(agent);

    return agent;
  }

  private static List<Tool> collectTools(Agent... agents) {
    List<Tool> out = new ArrayList<>();
    for (Agent a : agents) {
      if (a == null || a.getTools() == null) continue;
      out.addAll(a.getTools());
    }
    return out;
  }
}
