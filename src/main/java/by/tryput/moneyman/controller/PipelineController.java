package by.tryput.moneyman.controller;

import by.tryput.moneyman.domain.ExecutedPipeline;
import by.tryput.moneyman.dto.PipelineExecute;
import by.tryput.moneyman.dto.PipelineFromYaml;
import by.tryput.moneyman.service.ExecutePipelineService;
import by.tryput.moneyman.service.PipelineService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/pipeline", consumes = "text/yaml", produces = "text/yaml")
public class PipelineController {

    @Autowired
    PipelineService pipelineService;

    @Autowired
    ExecutePipelineService executePipelineService;

    @PostMapping(value = "/new")
    public ResponseEntity<Object> savePipeline(@RequestBody String pipeline) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            PipelineFromYaml pipelineFromYaml = mapper.readValue(pipeline, PipelineFromYaml.class);
            pipelineService.save(pipelineFromYaml);
            System.out.println(ReflectionToStringBuilder.toString(pipelineFromYaml, ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/execute")
    public String executePipeline(@RequestBody String yamlPipelineExecute) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        try {
            PipelineExecute pipelineFromYaml = mapper.readValue(yamlPipelineExecute, PipelineExecute.class);
            ExecutedPipeline executedPipeline = executePipelineService.execute(pipelineFromYaml);
            System.out.println(ReflectionToStringBuilder.toString(pipelineFromYaml, ToStringStyle.MULTI_LINE_STYLE));

            return mapper.writeValueAsString(executedPipeline);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Bad Request";
    }

    @GetMapping(value="/{executionId}/status", consumes = MediaType.ALL_VALUE)
    public String status(@PathVariable Long executionId) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.writeValueAsString(executePipelineService.status(executionId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Bad Request";
    }

    @GetMapping(value = "/{executionId}/stop", consumes = MediaType.ALL_VALUE)
    public String stop(@PathVariable Long executionId) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.writeValueAsString(executePipelineService.stop(executionId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Bad Request";
    }
}
