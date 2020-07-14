package edu.cnm.deepdive.quotes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Profile("preload")
public class Preloader implements CommandLineRunner {

  private static final String PRELOAD_DATA = "preload/quotes.json";

  private final QuoteRepository quoteRepository;

  @Autowired
  public Preloader(QuoteRepository quoteRepository) {
    this.quoteRepository = quoteRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    List<Quote> quotes = new LinkedList<>();
    Map<String, Source> sources = new HashMap<>();
    ClassPathResource resource = new ClassPathResource(PRELOAD_DATA);
    try (InputStream input = resource.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      for (Quote quote : mapper.readValue(input, Quote[].class)) {
        Source source = quote.getSource();
        if (source != null) {
          String name = source.getName();
          if (name != null) {
            name = name.toLowerCase();
            if (sources.containsKey(name)) {
              quote.setSource(sources.get(name));
            } else {
              sources.put(name, source);
            }
          }
        }
        quotes.add(quote);
      }
      quoteRepository.saveAll(quotes);
    }
  }

}
