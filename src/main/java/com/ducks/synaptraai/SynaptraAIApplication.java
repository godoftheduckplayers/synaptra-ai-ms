package com.ducks.synaptraai;

import com.ai.agentics.annotation.EnableAIAgentic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableAIAgentic
@SpringBootApplication
public class SynaptraAIApplication {

  public static void main(String[] args) {
    SpringApplication.run(SynaptraAIApplication.class, args);
  }
}
