package by.tryput.moneyman.repository;

import by.tryput.moneyman.domain.ExecutedPipeline;
import by.tryput.moneyman.repository.custom.ExecutedPipelineCustom;
import org.springframework.data.repository.CrudRepository;

public interface ExecutedPipelineRepository extends CrudRepository<ExecutedPipeline, Long>, ExecutedPipelineCustom {
}
