package edu.cnm.deepdive.quotes.service;

import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Tag;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class TagService {

  private final TagRepository tagRepository;
  private final QuoteRepository quoteRepository;

  @Autowired
  public TagService(TagRepository tagRepository,
      QuoteRepository quoteRepository) {
    this.tagRepository = tagRepository;
    this.quoteRepository = quoteRepository;
  }

  public Iterable<Tag> get() {
    return tagRepository.getAllByOrderByNameAsc();
  }

  public Tag get(long id) {
    return tagRepository.findById(id)
        .orElseThrow(NoSuchElementException::new);
  }

  public Iterable<Quote> getQuotes(@PathVariable long id) {
    return tagRepository.findById(id)
        .map(quoteRepository::getAllByTagsContainingOrderByTextAsc)
        .orElseThrow(NoSuchElementException::new);
  }

}
