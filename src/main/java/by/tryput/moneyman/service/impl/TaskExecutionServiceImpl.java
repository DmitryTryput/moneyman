package by.tryput.moneyman.service.impl;

import by.tryput.moneyman.domain.Status;
import by.tryput.moneyman.domain.TaskExecution;
import by.tryput.moneyman.repository.ExecutedPipelineRepository;
import by.tryput.moneyman.repository.TaskExecuteRepository;
import by.tryput.moneyman.service.ExecuteTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TaskExecutionServiceImpl implements ExecuteTaskService{

    @Autowired
    private TaskExecuteRepository taskExecuteRepository;

    @Autowired
    private ExecutedPipelineRepository executedPipelineRepository;

    public void execute(TaskExecution taskExecution, Long id) {
        if(executedPipelineRepository.getStatus(id) != Status.FAILED) {
            taskExecution.setStartTime(LocalDateTime.now());
            taskExecution.setStatus(Status.IN_PROGRESS);
            taskExecuteRepository.save(taskExecution);
            switch (taskExecution.getTask().getAction().getType()) {
                case COMPLETED:
                    completed(taskExecution);
                    break;
                case PRINT:
                    print(taskExecution);
                    break;
                case DELAYED:
                    delayed(taskExecution);
                    break;
                case RANDOM:
                    random(taskExecution);
            }
            taskExecution.setEndTime(LocalDateTime.now());
            taskExecuteRepository.save(taskExecution);
            System.out.println(executedPipelineRepository.getStatus(id));
            System.out.println(taskExecution.getStatus());
            executedPipelineRepository.setStatus(id, taskExecution.getStatus());
        }
    }

    private void print(TaskExecution taskExecution) {
        System.out.println(taskExecution.getTask().getName());
        taskExecution.setStatus(Status.COMPLETED);
    }

    private void random(TaskExecution taskExecution) {
        System.out.println(taskExecution.getTask().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Status status = Status.IN_PROGRESS;
        switch (new Random().nextInt(4) + 1) {
            case 1:
                status = Status.COMPLETED;
                break;
            case 2:
                status = Status.SKIPPED;
                break;
            case 3:
                status = Status.FAILED;
                break;
            case 4:
                status = Status.IN_PROGRESS;
        }
        taskExecution.setStatus(status);
    }

    private void completed(TaskExecution taskExecution) {
        System.out.println(taskExecution.getTask().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        taskExecution.setStatus(Status.COMPLETED);
    }

    private void delayed(TaskExecution taskExecution) {
        System.out.println(taskExecution.getTask().getName());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        taskExecution.setStatus(Status.COMPLETED);
    }
}