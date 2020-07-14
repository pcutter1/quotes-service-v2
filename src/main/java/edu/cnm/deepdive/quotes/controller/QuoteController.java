package edu.cnm.deepdive.quotes.controller;

import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.model.entity.User;
import edu.cnm.deepdive.quotes.service.QuoteService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quotes")
@ExposesResourceFor(Quote.class)
public class QuoteController {

  private final QuoteService quoteService;

  @Autowired
  public QuoteController(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Quote> post(@RequestBody Quote quote, Authentication auth) {
    User user = (User) auth.getPrincipal();
    quote.setContributor(user);
    quote = quoteService.create(quote);
    return ResponseEntity.created(quote.getHref()).body(quote);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Quote> get() {
    return quoteService.get();
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Quote> search(@RequestParam String q) {
    return quoteService.get(q);
  }

  @GetMapping(value = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Quote> getRandom() {
    return ResponseEntity.of(quoteService.getRandom());
  }

  @GetMapping(value = "/qod", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Quote> getQuoteOfDay(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.of(quoteService.get(date));
  }

  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Quote> get(@PathVariable long id) {
    return ResponseEntity.of(quoteService.get(id));
  }

  @PutMapping(value = "/{id:\\d+}",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Quote> put(@PathVariable long id, @RequestBody Quote quote) {
    return ResponseEntity.of(quoteService.update(id, quote));
  }

  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) {
    quoteService.delete(id);
  }

  @GetMapping(value = "/{id:\\d+}/text", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> getText(@PathVariable long id) {
    return ResponseEntity.of(quoteService.get(id).map(Quote::getText));
  }

  @PutMapping(value = "/{id:\\d+}/text",
      consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> putText(@PathVariable long id, @RequestBody String modifiedQuote) {
    return ResponseEntity.of(quoteService.update(id, modifiedQuote));
  }

  @GetMapping(value = "/{id:\\d+}/source", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> getAttribution(@PathVariable long id) {
    return ResponseEntity.of(quoteService.get(id).map(Quote::getSource));
  }

  @PutMapping(value = "/{id:\\d+}/source",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> putAttribution(@PathVariable long id, @RequestBody Source source) {
    return ResponseEntity.of(quoteService.update(id, source));
  }

  @PutMapping(value = "/{quoteId:\\d+}/source",
      consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<Long> putAttribution(@PathVariable long quoteId, @RequestBody long sourceId) {
    return ResponseEntity.of(quoteService.update(quoteId, sourceId).map(Source::getId));
  }

  @DeleteMapping(value = "/{id:\\d+}/source")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void clearAttribution(@PathVariable long id) {
    quoteService.clearSource(id);
  }

}
