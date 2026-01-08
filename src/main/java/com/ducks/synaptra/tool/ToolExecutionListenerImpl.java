package com.ducks.synaptra.tool;

import com.ducks.synaptra.event.tool.ToolExecutionListener;
import com.ducks.synaptra.event.tool.model.ToolExecutionRequest;
import com.ducks.synaptra.event.tool.model.ToolExecutionResponse;
import com.ducks.synaptra.event.tool.model.ToolExecutionStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class ToolExecutionListenerImpl implements ToolExecutionListener {

  @Override
  public ToolExecutionResponse onToolResponseEvent(ToolExecutionRequest toolExecutionRequest) {

    if (Objects.equals(
        toolExecutionRequest.getToolCall().function().name(), "get_daily_calorie_consumption")) {
      Map<String, Object> map = new HashMap<>();
      map.put("status", "operação realizada com sucesso");
      map.put("totalKcal", 520);
      return new ToolExecutionResponse(map, ToolExecutionStatus.SUCCESS);
    }

    if (Objects.equals(toolExecutionRequest.getToolCall().function().name(), "register_calories")) {
      Map<String, Object> map = new HashMap<>();
      map.put("status", "registro realizado com sucesso");
      return new ToolExecutionResponse(map, ToolExecutionStatus.SUCCESS);
    }

    if (Objects.equals(toolExecutionRequest.getToolCall().function().name(), "register_purchase")) {
      Map<String, Object> map = new HashMap<>();
      map.put("status", "compra registrada com sucesso");
      return new ToolExecutionResponse(map, ToolExecutionStatus.SUCCESS);
    }

    if (Objects.equals(toolExecutionRequest.getToolCall().function().name(), "list_purchases")) {
      Map<String, Object> map = new HashMap<>();
      map.put("item", "computador");
      map.put("valor", "17000");

      Map<String, Object> map1 = new HashMap<>();
      map1.put("item", "iphone");
      map1.put("valor", "5000");
      return new ToolExecutionResponse(List.of(map, map1), ToolExecutionStatus.SUCCESS);
    }

    return null;
  }
}
