package by.tryput.moneyman.dto;

import by.tryput.moneyman.domain.Task;
import by.tryput.moneyman.dto.deserializer.CustomMapValueDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class PipelineFromYaml {

    private String name;
    private String description;
    private List<Task> tasks;
    @JsonDeserialize(keyUsing = CustomMapValueDeserializer.class)
    private Map<ValueMap, String> transitions;
}

