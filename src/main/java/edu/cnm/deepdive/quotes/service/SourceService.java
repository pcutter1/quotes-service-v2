package edu.cnm.deepdive.quotes.service;

import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService {

  private final QuoteRepository quoteRepository;
  private final SourceRepository sourceRepository;

  @Autowired
  public SourceService(QuoteRepository quoteRepository,
      SourceRepository sourceRepository) {
    this.quoteRepository = quoteRepository;
    this.sourceRepository = sourceRepository;
  }

  public Source create(Source source) {
    return sourceRepository.save(source);
  }

  public Iterable<Source> get(boolean includeNull, boolean includeEmpty) {
    Iterable<Source> raw = includeEmpty
        ? sourceRepository.findAllByOrderByName()
        : sourceRepository.findAllWithQuotesOrderByName();
    if (includeNull) {
      List<Source> sources = new LinkedList<>();
      for (Source source : raw) {
        sources.add(source);
      }
      Source nullSource = new Source();
      for (Quote quote : quoteRepository.getAllBySourceOrderByTextAsc(null)) {
        nullSource.getQuotes().add(quote);
      }
      if (!nullSource.getQuotes().isEmpty()) {
        sources.add(nullSource);
      }
      return sources;
    } else {
      return raw;
    }
  }

  public Iterable<Source> get(String fragment) {
    return sourceRepository.getAllByNameContainsOrderByNameAsc(fragment);
  }

  public Optional<Source> get(long id) {
    return sourceRepository.findById(id);
  }

  public Optional<Source> update(long id, Source source) {
    return sourceRepository.findById(id).map((s) -> {
      s.setName(source.getName());
      return sourceRepository.save(s);
    });
  }

  public Optional<String> update(long id, String name) {
    return sourceRepository.findById(id).map((s) -> {
      s.setName(name);
      return sourceRepository.save(s).getName();
    });
  }

  public void delete(long id) {
    sourceRepository.findById(id).ifPresent((source) -> {
      List<Quote> quotes = source.getQuotes();
      quotes.forEach((quote) -> quote.setSource(null));
      quotes.clear();
      sourceRepository.delete(source);
    });
  }

}
