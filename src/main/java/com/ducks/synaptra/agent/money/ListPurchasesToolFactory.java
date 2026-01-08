package com.ducks.synaptra.agent.money;

import com.ducks.synaptra.client.openai.data.FunctionDef;
import com.ducks.synaptra.client.openai.data.Parameter;
import com.ducks.synaptra.client.openai.data.ParameterProperty;
import com.ducks.synaptra.client.openai.data.Tool;
import com.ducks.synaptra.model.agent.Agent;

/** Registers a tool that lists recorded purchases. */
public final class ListPurchasesToolFactory {

  private ListPurchasesToolFactory() {}

  public static void registerListPurchasesTool(Agent agent) {
    if (agent == null) throw new IllegalArgumentException("agent must not be null");

    Parameter parameter = new Parameter();

    parameter.addProperty(
        "dateFrom",
        new ParameterProperty("string", "Optional start date in ISO-8601 format (yyyy-MM-dd)."),
        false);

    parameter.addProperty(
        "dateTo",
        new ParameterProperty("string", "Optional end date in ISO-8601 format (yyyy-MM-dd)."),
        false);

    parameter.addProperty(
        "limit",
        new ParameterProperty(
            "integer", "Optional max number of items to return. Defaults to 20 if omitted."),
        false);

    if (agent.isSupportsInterimMessages()) {
      parameter.addProperty(
          "response",
          new ParameterProperty(
              "string", "Intermediate message informing the user purchases are being retrieved."),
          true);
    }

    FunctionDef fn =
        new FunctionDef(
            "list_purchases",
            "List recorded purchases.\n"
                + "If the user asks for a specific period but dates are ambiguous, ask ONE clarification question and wait.",
            parameter);

    agent.addTool(new Tool(fn));
  }
}
