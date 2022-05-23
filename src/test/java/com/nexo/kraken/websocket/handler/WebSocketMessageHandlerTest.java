package com.nexo.kraken.websocket.handler;

import com.nexo.kraken.websocket.model.TickerResponse;
import com.nexo.kraken.websocket.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;

class WebSocketMessageHandlerTest {
    private WebSocketMessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        messageHandler = WebSocketMessageHandler.instance();
    }

    @Test
    void instance() {
        WebSocketMessageHandler instance1 = WebSocketMessageHandler.instance();
        WebSocketMessageHandler instance2 = WebSocketMessageHandler.instance();
        assertSame(instance1, instance2);
    }

    @Test
    void handle() {
        String tickerMessage = TestUtil.getTickerMessage();
        TickerResponse response = messageHandler.parse(tickerMessage);

        assertAll(
                () -> assertThat(response.getAsks().entries()).hasSize(1),
                () -> assertThat(response.getBids().entries().stream()
                        .map(Map.Entry::getValue).findFirst()).isEqualTo(Optional.of("5525.10000, 1.000")),
                () -> assertThat(response.getAsks().entries().stream()
                        .map(Map.Entry::getValue).findFirst()).isEqualTo(Optional.of("5525.40000, 1.000")),
                () -> assertThat(response.getAsks().entries()).hasSize(1),
                () -> assertThat(response.getBids().entries()).hasSize(1),
                () -> assertThat(response.getDateTime()).isNotNull()
        );
    }
}