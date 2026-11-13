package ru.semykin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

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
