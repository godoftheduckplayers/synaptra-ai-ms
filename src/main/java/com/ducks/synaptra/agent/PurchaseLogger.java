package com.ducks.synaptra.agent;

import com.ducks.synaptra.client.openai.data.Parameter;
import com.ducks.synaptra.client.openai.data.ParameterProperty;
import com.ducks.synaptra.client.openai.data.Tool;
import com.ducks.synaptra.client.openai.data.ToolChoice;
import com.ducks.synaptra.tool.model.ExcelTool;
import java.util.List;

public class PurchaseLogger extends BaseAgent {

  @Override
  public String getIdentifier() {
    return "00d62c0b-430c-41ce-a819-cfb4703cef77";
  }

  @Override
  public String getName() {
    return "PurchaseLogger";
  }

  @Override
  public String getGoal() {
    return "Purchase Activity Logging Agent";
  }

  @Override
  public AgentType getAgentType() {
    return AgentType.AGENT;
  }

  @Override
  public ProviderConfig getProviderConfig() {
    return new ProviderConfig("gpt-4o-mini", 0.2, 256, 1.0);
  }

  @Override
  public String getPrompt() {

    return """
          You are a $name

          $goal

          You must strictly follow the procedure defined by the available tool.
          All questions asked to the user must be derived exclusively from the tool parameters.

          Do not request information outside the defined procedure.
          The task is complete only when the purchase is successfully registered.

          Sempre pergunte todos os dados necessários para o cliente, nunca faça um auto preenchimento dos campos
        """;
  }

  @Override
  public List<Tool> getTools() {
    List<Tool> tools = super.getTools();
    Parameter parameter = new Parameter();
    parameter.addProperty(
        "item", new ParameterProperty("string", "Descrição do item/compra."), true);
    parameter.addProperty("valor", new ParameterProperty("number", "Valor total da compra"), true);
    parameter.addProperty("dataCompra", new ParameterProperty("string", "Data da compra."), true);

    ExcelTool excelTool =
        new ExcelTool(
            "extract_purchase_data",
            "Função de registrar compras, essa função é chamada para registrar compras e salvar em um excel",
            parameter);

    tools.add(new Tool(excelTool));
    return tools;
  }

  @Override
  public List<Agent> getAgents() {
    return List.of();
  }

  @Override
  public ToolChoice getToolChoice() {
    return ToolChoice.AUTO;
  }
}
