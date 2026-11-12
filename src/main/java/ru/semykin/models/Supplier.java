package ru.semykin.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
@JsonDeserialize
public class Supplier {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("baseUrl")
    private String baseUrl;
    @JsonProperty("apiKey")
    private String apiKey;
}
