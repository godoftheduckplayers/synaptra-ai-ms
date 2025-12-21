package com.ducks.synaptraai.config;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class UuidHandshakeHandler extends DefaultHandshakeHandler {

  @Override
  protected Principal determineUser(
      @NonNull ServerHttpRequest request,
      @NonNull WebSocketHandler wsHandler,
      @NonNull Map<String, Object> attributes) {

    String uuid = UUID.randomUUID().toString();
    return () -> uuid;
  }
}
