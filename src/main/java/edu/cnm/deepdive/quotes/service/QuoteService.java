package edu.cnm.deepdive.quotes.service;

import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.model.entity.Tag;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

  private final QuoteRepository quoteRepository;
  private final SourceRepository sourceRepository;
  private final TagRepository tagRepository;

  @Autowired
  public QuoteService(QuoteRepository quoteRepository, SourceRepository sourceRepository,
      TagRepository tagRepository) {
    this.quoteRepository = quoteRepository;
    this.sourceRepository = sourceRepository;
    this.tagRepository = tagRepository;
  }

  public Quote create(Quote quote) {
    quote.setSource(resolveSource(quote.getSource()));
    List<Tag> resolvedTags = resolveTags(quote.getTags());
    quote.getTags().clear();
    quote.getTags().addAll(resolvedTags);
    return quoteRepository.save(quote);
  }

  public Iterable<Quote> get() {
    return quoteRepository.getAllByOrderByTextAsc();
  }

  public Iterable<Quote> get(String fragment) {
    return quoteRepository.getAllByTextContainsOrderByTextAsc(fragment);
  }

  public Optional<Quote> getRandom() {
    return quoteRepository.getRandom();
  }

  public Optional<Quote> get(Long id) {
    return quoteRepository.findById(id);
  }

  public Optional<Quote> get(LocalDate date) {
    long count = quoteRepository.getCount();
    long dayOffset = date.toEpochDay() % count;
    if (dayOffset < 0) {
      dayOffset += count;
    }
    return quoteRepository.findAll(PageRequest.of((int) dayOffset, 1, Sort.by(Direction.ASC, "id")))
        .get().findFirst();
  }

  public Optional<Quote> update(long quoteId, Quote quote) {
    return quoteRepository.findById(quoteId).map((q) -> {
      q.setSource(resolveSource(quote.getSource()));
      q.setText(quote.getText());
      return quoteRepository.save(q);
    });
  }

  public Optional<String> update(long quoteId, String text) {
    return quoteRepository.findById(quoteId).map((quote) -> {
      quote.setText(text);
      return quoteRepository.save(quote).getText();
    });
  }

  public Optional<Source> update(long quoteId, Source source) {
    return quoteRepository.findById(quoteId).map((quote) -> {
      quote.setSource(resolveSource(source));
      return quoteRepository.save(quote).getSource();
    });
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public Optional<Source> update(long quoteId, long sourceId) {
    return quoteRepository.findById(quoteId).map((quote) -> {
      quote.setSource(sourceRepository.findById(sourceId).get());
      return quoteRepository.save(quote).getSource();
    });
  }

  public void clearSource(long quoteId) {
    quoteRepository.findById(quoteId).map((quote) -> {
      quote.setSource(null);
      return quoteRepository.save(quote);
    })
    .orElseThrow(NoSuchElementException::new);
  }

  public void delete(long id) {
    if (quoteRepository.existsById(id)) {
      quoteRepository.deleteById(id);
    }
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private Source resolveSource(Source source) {
    Long sourceId;
    return (source != null && (sourceId = source.getId()) != null)
        ? sourceRepository.findById(sourceId).get()
        : source;
  }

  private List<Tag> resolveTags(List<Tag> raw) {
    return raw.stream()
        .map((tag) -> (tag.getId() == null) ?
            tag : tagRepository.findById(tag.getId()).orElseThrow(NoSuchElementException::new))
        .collect(Collectors.toList());
  }

}
