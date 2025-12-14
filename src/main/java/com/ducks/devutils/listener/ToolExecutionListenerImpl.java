package com.ducks.devutils.listener;

import com.ai.agentics.orchestration.event.tool.ToolExecutionListener;
import com.ai.agentics.orchestration.event.tool.contract.ToolResponseEvent;
import org.springframework.stereotype.Service;

@Service
public class ToolExecutionListenerImpl implements ToolExecutionListener {

  @Override
  public void onToolExecutionResponseEvent(ToolResponseEvent toolResponseEvent) {
    System.out.println(toolResponseEvent);
  }
}
