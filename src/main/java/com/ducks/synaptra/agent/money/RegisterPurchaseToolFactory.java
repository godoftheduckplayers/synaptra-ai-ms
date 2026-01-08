package com.ducks.synaptra.agent.money;

import com.ducks.synaptra.client.openai.data.FunctionDef;
import com.ducks.synaptra.client.openai.data.Parameter;
import com.ducks.synaptra.client.openai.data.ParameterProperty;
import com.ducks.synaptra.client.openai.data.Tool;
import com.ducks.synaptra.model.agent.Agent;

/** Registers a tool that records a purchase entry. */
public final class RegisterPurchaseToolFactory {

  private RegisterPurchaseToolFactory() {}

  public static void registerRegisterPurchaseTool(Agent agent) {
    if (agent == null) throw new IllegalArgumentException("agent must not be null");

    Parameter parameter = new Parameter();

    parameter.addProperty(
        "item", new ParameterProperty("string", "Purchased item/description."), true);

    parameter.addProperty(
        "amount", new ParameterProperty("number", "Total purchase amount."), true);

    parameter.addProperty(
        "date",
        new ParameterProperty(
            "string", "Purchase date in ISO-8601 format (yyyy-MM-dd). Example: 2026-01-08."),
        true);

    parameter.addProperty(
        "paymentType",
        new ParameterProperty("string", "Payment type. Allowed values: CASH, PIX, CARD."),
        true);

    parameter.addProperty(
        "cardMode",
        new ParameterProperty(
            "string", "If paymentType=CARD, specify: DEBIT or CREDIT. Otherwise omit."),
        false);

    parameter.addProperty(
        "installments",
        new ParameterProperty(
            "integer",
            "If paymentType=CARD and cardMode=CREDIT and purchase is installment-based, specify number of installments. Otherwise omit."),
        false);

    if (agent.isSupportsInterimMessages()) {
      parameter.addProperty(
          "response",
          new ParameterProperty(
              "string", "Intermediate message informing the user the purchase is being recorded."),
          true);
    }

    FunctionDef fn =
        new FunctionDef(
            "register_purchase",
            "Record a purchase entry.\n"
                + "Do not execute until required fields are explicitly provided.\n"
                + "Ask only ONE clarification question at a time if something required is missing.",
            parameter);

    agent.addTool(new Tool(fn));
  }
}
