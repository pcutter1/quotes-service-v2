package edu.cnm.deepdive.quotes.service;

import edu.cnm.deepdive.quotes.model.entity.Source;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SourceRepository extends JpaRepository<Source, Long> {

  Optional<Source> findFirstByName(String name);

  Iterable<Source> findAllByOrderByName();

  @Query("SELECT DISTINCT s FROM Source AS s WHERE EXISTS (SELECT q FROM Quote AS q WHERE q.source = s) ORDER BY s.name")
  Iterable<Source> findAllWithQuotesOrderByName();

  Iterable<Source> getAllByNameContainsOrderByNameAsc(String fragment);

}
