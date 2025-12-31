package com.ducks.synaptra.tool.model;

import com.ducks.synaptra.client.openai.data.FunctionDef;
import com.ducks.synaptra.client.openai.data.Parameter;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ExcelTool extends FunctionDef implements Tool {

  private final String uuid;

  public ExcelTool(String name, String description, Parameter parameters) {
    super(name, description, parameters);
    this.uuid = UUID.randomUUID().toString();
  }

  @Override
  public ToolType type() {
    return ToolType.EXCEL;
  }
}
