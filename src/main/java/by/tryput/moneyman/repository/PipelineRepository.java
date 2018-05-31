package by.tryput.moneyman.repository;

import by.tryput.moneyman.domain.Pipeline;
import org.springframework.data.repository.CrudRepository;

public interface PipelineRepository extends CrudRepository<Pipeline, Long> {

    Pipeline getFirstByNameContainsAndDescriptionContainsIgnoreCase(String name, String description);
}
