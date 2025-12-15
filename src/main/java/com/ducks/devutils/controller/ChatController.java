package com.ducks.devutils.controller;

import java.security.Principal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

  private final SimpMessagingTemplate template;

  public ChatController(SimpMessagingTemplate template) {
    this.template = template;
  }

  @MessageMapping("/chat.send")
  public void send(ChatMsg msg, Principal principal) {
    // Broadcast para a conversa
    template.convertAndSend("/topic/conversations/" + msg.conversationId(), msg);

    // Eco privado por device (só pra testar Pattern C)
    // Se você não tiver Principal (sem security), use msg.from() como username (apenas em dev)
    String username = (principal != null ? principal.getName() : msg.from());
    template.convertAndSendToUser(username, "/queue/inbox." + msg.deviceId(), msg);
  }

  public record ChatMsg(
      String conversationId, String from, String channel, String deviceId, String text, long ts) {}
}
