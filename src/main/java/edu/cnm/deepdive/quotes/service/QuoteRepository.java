package edu.cnm.deepdive.quotes.service;

import edu.cnm.deepdive.quotes.model.entity.Quote;
import edu.cnm.deepdive.quotes.model.entity.Source;
import edu.cnm.deepdive.quotes.model.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

  Iterable<Quote> getAllByOrderByCreatedDesc();

  Iterable<Quote> getAllByOrderByTextAsc();

  Iterable<Quote> getAllByTextContainsOrderByTextAsc(String fragment);

  Iterable<Quote> getAllByTagsContainingOrderByTextAsc(Tag tag);

  @Query(value = "SELECT * FROM sa.Quote ORDER BY RANDOM() OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY",
      nativeQuery = true)
  Optional<Quote> getRandom();

  @Query(value = "SELECT COUNT(q) FROM Quote AS q")
  long getCount();

  Iterable<Quote> getAllBySourceOrderByTextAsc(Source source);

}
