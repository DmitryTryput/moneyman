package by.tryput.moneyman.service.impl;

import by.tryput.moneyman.domain.Pipeline;
import by.tryput.moneyman.domain.Transition;
import by.tryput.moneyman.dto.PipelineFromYaml;
import by.tryput.moneyman.repository.ExecutedPipelineRepository;
import by.tryput.moneyman.repository.PipelineRepository;
import by.tryput.moneyman.service.ExecutePipelineService;
import by.tryput.moneyman.service.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineServiceImpl implements PipelineService {

    @Autowired
    PipelineRepository pipelineRepository;

    @Autowired
    ExecutePipelineService executePipelineService;

    @Autowired
    ExecutedPipelineRepository executedPipelineRepository;

    @Override
    public void save(PipelineFromYaml pipeline) {
        Pipeline pipelineToSave = new Pipeline();
        pipelineToSave.setDescription(pipeline.getDescription());
        pipelineToSave.setName(pipeline.getName());
        pipeline.getTasks().forEach(t -> pipelineToSave.getTasks().add(t));
        pipeline.getTransitions().forEach((k, v) ->
                pipelineToSave.getTransitions().add(new Transition(k.getKey(), v)));
        pipelineRepository.save(pipelineToSave);
    }

}
