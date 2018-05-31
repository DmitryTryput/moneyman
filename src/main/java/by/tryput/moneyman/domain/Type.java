package by.tryput.moneyman.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Type {
    @JsonProperty("print")
    PRINT,
    @JsonProperty("random")
    RANDOM,
    @JsonProperty("completed")
    COMPLETED,
    @JsonProperty("delayed")
    DELAYED
}
