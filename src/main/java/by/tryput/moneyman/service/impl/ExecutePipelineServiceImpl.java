package by.tryput.moneyman.service.impl;

import by.tryput.moneyman.domain.ExecutedPipeline;
import by.tryput.moneyman.domain.Pipeline;
import by.tryput.moneyman.domain.Status;
import by.tryput.moneyman.domain.TaskExecution;
import by.tryput.moneyman.domain.Transition;
import by.tryput.moneyman.dto.PipelineExecute;
import by.tryput.moneyman.repository.ExecutedPipelineRepository;
import by.tryput.moneyman.repository.PipelineRepository;
import by.tryput.moneyman.service.ExecutePipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

@Service
public class ExecutePipelineServiceImpl implements ExecutePipelineService {

    @Autowired
    ExecutedPipelineRepository executedPipelineRepository;

    @Autowired
    PipelineRepository pipelineRepository;

    @Autowired
    TaskExecutionServiceImpl taskExecutionService;

    public ExecutedPipeline execute(PipelineExecute pipelineExecute) {
        ExecutedPipeline executedPipeline = getExecutedPipeline(pipelineExecute);
        executedPipelineRepository.save(executedPipeline);
        new Thread(() -> {
            Transition start = executedPipeline.getPipeline().getTransitions().remove(0);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
            concurrencyExecute(executedPipeline, start, cyclicBarrier);
            executedPipeline.setEndTime(LocalDateTime.now());
            executedPipelineRepository.save(executedPipeline);
        }).start();
        return executedPipeline;
    }

    private void concurrencyExecute(ExecutedPipeline executedPipeline, Transition start, CyclicBarrier cyclicBarrier) {
        taskExecutionService.execute(executedPipeline.getTasks().stream()
                .filter(t -> t.getTask().getName().equals(start.getFromTask())).findFirst().get(), executedPipeline.getExecutionId());
        if (executedPipeline.getPipeline().getTransitions().stream()
                .anyMatch(t -> Objects.equals(t.getFromTask(), start.getFromTask()))) {
            new Thread(() -> {
                Transition concurrencyTransitionRemove = executedPipeline.getPipeline().getTransitions()
                        .stream().filter(t -> t.getFromTask().equals(start.getFromTask())).findFirst().get();
                executedPipeline.getPipeline().getTransitions().remove(concurrencyTransitionRemove);
                moveOnTransations(executedPipeline, concurrencyTransitionRemove, cyclicBarrier);
            }).start();
        }
        moveOnTransations(executedPipeline, start, cyclicBarrier);
    }

    private void moveOnTransations(ExecutedPipeline executedPipeline, Transition concurrencyTransitionRemove, CyclicBarrier cyclicBarrier) {
        if (executedPipeline.getPipeline().getTransitions()
                .stream().anyMatch(t -> t.getFromTask().equals(concurrencyTransitionRemove.getToTask()))) {
            Transition concurrencyTransitionExecute = executedPipeline.getPipeline().getTransitions()
                    .stream().filter(t -> t.getFromTask().equals(concurrencyTransitionRemove.getToTask())).findFirst().get();
            executedPipeline.getPipeline().getTransitions().remove(concurrencyTransitionExecute);
            concurrencyExecute(executedPipeline, concurrencyTransitionExecute, cyclicBarrier);
        } else {
            try {
                cyclicBarrier.await();
                taskExecutionService.execute(executedPipeline.getTasks().stream()
                        .filter(t -> t.getTask().getName().equals(concurrencyTransitionRemove.getToTask())).findFirst().get(), executedPipeline.getExecutionId());
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public ExecutedPipeline status(Long id) {
        return executedPipelineRepository.findById(id).get();
    }

    @Override
    public ExecutedPipeline stop(Long id) {
        ExecutedPipeline executedPipeline = executedPipelineRepository.findById(id).get();
        if (executedPipeline.getStatus() != Status.COMPLETED) {
            executedPipeline.setStatus(Status.FAILED);
            executedPipelineRepository.save(executedPipeline);
        }
        return executedPipeline;
    }

    private ExecutedPipeline getExecutedPipeline(PipelineExecute pipelineExecute) {
        Pipeline pipeline = pipelineRepository
                .getFirstByNameContainsAndDescriptionContainsIgnoreCase(pipelineExecute.getPipelineName(),
                        pipelineExecute.getContext());
        ExecutedPipeline executedPipeline = new ExecutedPipeline();
        executedPipeline.setPipeline(pipeline);
        executedPipeline.getTasks().addAll(pipeline.getTasks()
                .stream().map(TaskExecution::new).collect(Collectors.toList()));
        executedPipeline.setStartTime(LocalDateTime.now());
        return executedPipeline;
    }
}
