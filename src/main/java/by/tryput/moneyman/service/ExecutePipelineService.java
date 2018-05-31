package by.tryput.moneyman.service;

import by.tryput.moneyman.domain.ExecutedPipeline;
import by.tryput.moneyman.dto.PipelineExecute;

public interface ExecutePipelineService {

    ExecutedPipeline execute(PipelineExecute pipelineExecute);

    ExecutedPipeline status(Long id);

    ExecutedPipeline stop(Long id);
}
