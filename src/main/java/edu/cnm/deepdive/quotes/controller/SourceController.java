package edu.cnm.deepdive.quotes.controller;

import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/sources")
@ExposesResourceFor(Source.class)
public class SourceController {

  private final SourceService sourceService;

  @Autowired
  public SourceController(SourceService sourceService) {
    this.sourceService = sourceService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> post(@RequestBody Source source) {
    source = sourceService.create(source);
    return ResponseEntity.created(source.getHref()).body(source);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Source> get(
      @RequestParam(required = false, defaultValue = "false") boolean includeNull,
      @RequestParam(required = false, defaultValue = "true") boolean includeEmpty) {
    return sourceService.get(includeNull, includeEmpty);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Source> search(@RequestParam String q) {
    return sourceService.get(q);
  }

  @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> get(@PathVariable long id) {
    return ResponseEntity.of(sourceService.get(id));
  }

  @PutMapping(value = "/{id:\\d+}",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> put(@PathVariable long id, @RequestBody Source source) {
    return ResponseEntity.of(sourceService.update(id, source));
  }

  @GetMapping(value = "/{id:\\d+}/name", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> getName(@PathVariable long id) {
    return ResponseEntity.of(sourceService.get(id).map(Source::getName));
  }

  @PutMapping(value = "/{id:\\d+}/name",
      consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> putName(@PathVariable long id, @RequestBody String updatedName) {
    return ResponseEntity.of(sourceService.update(id, updatedName));
  }

  @DeleteMapping(value = "/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) {
    sourceService.delete(id);
  }

}
