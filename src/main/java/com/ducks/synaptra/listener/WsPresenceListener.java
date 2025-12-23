package com.ducks.synaptra.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WsPresenceListener {

  @EventListener
  public void onConnect(SessionConnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    log.info(
        "WS CONNECT sessionId={} user={}",
        sha.getSessionId(),
        sha.getUser() != null ? sha.getUser().getName() : null);
  }

  @EventListener
  public void onDisconnect(SessionDisconnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    log.info(
        "WS DISCONNECT sessionId={} user={}",
        sha.getSessionId(),
        sha.getUser() != null ? sha.getUser().getName() : null);
  }
}
