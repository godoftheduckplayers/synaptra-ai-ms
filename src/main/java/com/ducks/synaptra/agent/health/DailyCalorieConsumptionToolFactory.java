package com.ducks.synaptra.agent.health;

import com.ducks.synaptra.client.openai.data.FunctionDef;
import com.ducks.synaptra.client.openai.data.Parameter;
import com.ducks.synaptra.client.openai.data.ParameterProperty;
import com.ducks.synaptra.client.openai.data.Tool;
import com.ducks.synaptra.model.agent.Agent;

/**
 * Registers a tool that retrieves the user's total calorie consumption (kcal) for a specific day.
 *
 * <p>Field collected: date
 */
public final class DailyCalorieConsumptionToolFactory {

  private DailyCalorieConsumptionToolFactory() {}

  public static void registerDailyConsumptionTool(Agent agent) {
    Parameter parameter = getParameter(agent);

    FunctionDef getDailyCaloriesFunction =
        new FunctionDef(
            "get_daily_calorie_consumption",
            "Retrieve the user's total calorie consumption (kcal) for a specific day.\n"
                + "Do not execute this function until the required parameter 'date' has been explicitly provided.\n"
                + "If the user mentions a relative day (e.g., today, yesterday) or an ambiguous date, ask a single clarification question and wait.",
            parameter);

    agent.addTool(new Tool(getDailyCaloriesFunction));
  }

  private static Parameter getParameter(Agent agent) {
    Parameter parameter = new Parameter();

    parameter.addProperty(
        "date",
        new ParameterProperty(
            "string",
            "Target day to retrieve calorie consumption, in ISO-8601 format (yyyy-MM-dd). "
                + "Example: 2026-01-06."),
        true);

    // Optional: interim UX message (same pattern you used in CalorieIntakeToolFactory)
    if (agent.isSupportsInterimMessages()) {
      parameter.addProperty(
          "response",
          new ParameterProperty(
              "string",
              "Generates an intermediate response informing the user that the system is retrieving the calorie consumption for the requested day."),
          true);
    }
    return parameter;
  }
}
