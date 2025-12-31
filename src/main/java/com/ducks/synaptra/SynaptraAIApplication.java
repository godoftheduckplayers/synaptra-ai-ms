package com.ducks.synaptra;

import com.ducks.synaptra.annotation.EnableAIAgenticTool;
import com.ducks.synaptra.annotation.EnableSynaptraLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableAIAgenticTool
@EnableSynaptraLog
@SpringBootApplication
public class SynaptraAIApplication {

  public static void main(String[] args) {
    SpringApplication.run(SynaptraAIApplication.class, args);
  }
}
