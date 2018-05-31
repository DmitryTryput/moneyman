package by.tryput.moneyman.service;

import by.tryput.moneyman.domain.Status;
import by.tryput.moneyman.domain.TaskExecution;

public interface ExecuteTaskService {

    void execute(TaskExecution taskExecution, Long id);
}
