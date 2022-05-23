package com.nexo.kraken.websocket.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KrakenApi {
    private String privateEndpoint;
    private String publicEndpoint;
    private String baseDomain;
    private String privatePath;
    private String authTokenPath;
}
