package com.nexo.kraken.websocket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subscription {

    @JsonProperty("name")
    @Builder.Default
    private String name = "ticker";

    @JsonProperty("token")
    private String token;
}
