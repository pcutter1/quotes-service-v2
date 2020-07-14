package edu.cnm.deepdive.quotes;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class PreloadLauncher {

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(QuotesApplication.class)
        .profiles("preload")
        .run(args);
  }


}
