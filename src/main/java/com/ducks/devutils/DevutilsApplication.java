package com.ducks.devutils;

import com.ai.agentics.annotation.EnableAIAgentic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableAIAgentic
@SpringBootApplication
public class DevutilsApplication {

  public static void main(String[] args) {
    SpringApplication.run(DevutilsApplication.class, args);
  }
}
