package com.ducks.synaptra.agent.health;

import com.ducks.synaptra.client.openai.data.FunctionDef;
import com.ducks.synaptra.client.openai.data.Parameter;
import com.ducks.synaptra.client.openai.data.ParameterProperty;
import com.ducks.synaptra.client.openai.data.Tool;
import com.ducks.synaptra.model.agent.Agent;

/**
 * Registers a tool that collects and records nutrition macro data for a calorie intake entry.
 *
 * <p>Fields collected: prot, carb, fat, sat, fibra, kcal
 */
public final class CalorieIntakeToolFactory {

  private CalorieIntakeToolFactory() {}

  public static void registerCaloriesTool(Agent agent) {
    Parameter parameter = new Parameter();

    parameter.addProperty(
        "prot",
        new ParameterProperty(
            "number",
            "Protein amount for the intake entry. Provide a numeric value (typically grams)."),
        true);

    parameter.addProperty(
        "carb",
        new ParameterProperty(
            "number",
            "Carbohydrates amount for the intake entry. Provide a numeric value (typically grams)."),
        true);

    parameter.addProperty(
        "fat",
        new ParameterProperty(
            "number",
            "Total fat amount for the intake entry. Provide a numeric value (typically grams)."),
        true);

    parameter.addProperty(
        "sat",
        new ParameterProperty(
            "number",
            "Saturated fat amount for the intake entry. Provide a numeric value (typically grams)."),
        true);

    parameter.addProperty(
        "fibra",
        new ParameterProperty(
            "number",
            "Dietary fiber amount for the intake entry. Provide a numeric value (typically grams)."),
        true);

    parameter.addProperty(
        "kcal",
        new ParameterProperty(
            "number", "Total calories for the intake entry. Provide a numeric value (kcal)."),
        true);

    // Optional: if you also want an interim UX message (same pattern as your route tool)
    if (agent.isSupportsInterimMessages()) {
      parameter.addProperty(
          "response",
          new ParameterProperty(
              "string",
              "Generates an intermediate response informing the user that the nutrition entry is being registered."),
          true);
    }

    FunctionDef registerCaloriesFunction =
        new FunctionDef(
            "register_calories",
            "Collect and register nutrition macro values for a calorie intake entry.\n"
                + "Do not execute this function until all required parameters have been explicitly provided.\n"
                + "If any required value is missing, request it individually and wait before proceeding.",
            parameter);

    agent.addTool(new Tool(registerCaloriesFunction));
  }
}
