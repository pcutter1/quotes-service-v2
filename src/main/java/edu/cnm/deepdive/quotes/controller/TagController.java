package edu.cnm.deepdive.quotes.controller;

import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Tag;
import edu.cnm.deepdive.quotes.service.TagService;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
@ExposesResourceFor(Tag.class)
public class TagController {

  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }


  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Tag> get() {
    return tagService.get();
  }

  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Tag get(@PathVariable long id) {
    return tagService.get(id);
  }

  @GetMapping(value = "/{id:\\d+}/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Quote> getQuotes(@PathVariable long id) {
    return tagService.getQuotes(id);
  }

}
