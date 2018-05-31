package by.tryput.moneyman.repository.custom;

import by.tryput.moneyman.domain.Status;

public interface ExecutedPipelineCustom {

    Status getStatus(Long id);

    void setStatus(Long id, Status status);
}
