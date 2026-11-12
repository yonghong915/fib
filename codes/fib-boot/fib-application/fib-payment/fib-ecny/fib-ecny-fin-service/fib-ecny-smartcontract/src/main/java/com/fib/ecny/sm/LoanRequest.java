package com.fib.ecny.sm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class LoanRequest {
    @JsonProperty("tranCode")
    private String tranCode;

    @JsonProperty("msgBody")
    JsonNode msgBody;
}
