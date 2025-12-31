package com.ducks.synaptra.tool;

import com.ducks.synaptra.orchestration.event.record.RecordExecutionEvent;
import com.ducks.synaptra.orchestration.event.record.contract.RecordRequestEvent;
import com.ducks.synaptra.orchestration.event.tool.ToolExecutionListener;
import com.ducks.synaptra.orchestration.event.tool.contract.ToolResponseEvent;
import com.ducks.synaptra.prompt.RecordEventPublisher;
import com.ducks.synaptra.prompt.contract.RecordEvent;
import com.ducks.synaptra.service.excel.ExcelAppendService;
import com.ducks.synaptra.service.excel.ExcelTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExcelToolService implements ToolExecutionListener {

  private final ExcelTemplateService templateService;
  private final ExcelAppendService appendService;
  private final RecordEventPublisher publisher;

  @Override
  public void onToolExecutionResponseEvent(ToolResponseEvent toolResponseEvent) {
    if ("extract_purchase_data".equals(toolResponseEvent.toolCall().function().name())) {
      System.out.println(toolResponseEvent);
      // preciso gerar um novo evento de executando tool, para ter o tool finalizada
      // e incluir isso na execução do agente
      // publisher.publishEvent(toolResponseEvent);
      publisher.publishEvent(
          new RecordRequestEvent(
              toolResponseEvent.sessionId(),
              toolResponseEvent.agent(),
              toolResponseEvent.user(),
              new RecordEvent(
                  "Tool executed successfully.", RecordExecutionEvent.FINISHED_TOOL_EXECUTION)));


      //      List<ExcelColumn> columns =
      //          List.of(
      //              new ExcelColumn("id", "ID", 10, true),
      //              new ExcelColumn("name", "Nome", 28, true),
      //              new ExcelColumn("email", "E-mail", 32, true),
      //              new ExcelColumn("createdAt", "Criado em", 22, true));
      //
      //      // templateService.createTemplate("clientes.xlsx", "clientes", columns);
      //
      //      List<Map<String, Object>> rows = List.of(new HashMap<>());
      //      rows.forEach(
      //          c ->
      //              c.putAll(
      //                  Map.of(
      //                      "id", "uuid",
      //                      "name", "nome",
      //                      "email", "email",
      //                      "createdAt", "now")));
      //
      //      // appendService.append("clientes.xlsx", "clientes", columns, rows);
    }
  }
}
