package com.nexo.kraken.websocket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponse {
    @JsonProperty("error")
    private List<String> error;
    @JsonProperty("result")
    private AuthToken result;
}
