package com.nexo.kraken.websocket.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("nexo.kraken.websocket")
@Component
@Data
public class KrakenConfigProperties {
    private KrakenApi api;
    private KrakenWss wss;
    private KrakenAuth auth;
    private KrakenMessage message;
    private KrakenConnection connection;
}
