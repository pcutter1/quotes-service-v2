package edu.cnm.deepdive.quotes;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class ServerLauncher {

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(QuotesApplication.class)
        .profiles("server")
        .run(args);
  }

}
