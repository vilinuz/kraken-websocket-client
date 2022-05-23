package com.nexo.kraken.websocket.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.kraken.websocket.handler.WebSocketMessageHandler;
import com.nexo.kraken.websocket.model.SubscriptionStatus;
import com.nexo.kraken.websocket.model.TickerResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.http.WebSocket;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketClientListener implements WebSocket.Listener {
    private final WebSocketMessageHandler webSocketMessageHandler;
    private final ObjectMapper objectMapper;
    private final CountDownLatch latch;

    private static WebSocketClientListener instance;

    @Override
    public void onOpen(WebSocket ws) {
        log.info("WebSocket Client session opened. Sub-protocol: {}", ws.getSubprotocol());
    }

    @Override
    public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last) {
        latch.countDown();
        handle(ws, data.toString());
        return WebSocket.Listener.super.onText(ws, data, last);
    }

    @SneakyThrows
    private void handle(final WebSocket ws, final String message) {
        if (!ws.isOutputClosed()) {
            if (isSubscriptionStatusMessage(message)) {
                SubscriptionStatus status = objectMapper.readValue(message, SubscriptionStatus.class);
                log.info(status.toString());
            } else if (isTickerMessage(message)) {
                TickerResponse response = webSocketMessageHandler.parse(message);
                log.info("Ticker response:\n{}", response);
            } else {
                log.warn("Unknown message: {}", message);
            }
        }
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        return null;
    }

    public static WebSocketClientListener create(WebSocketMessageHandler handler, ObjectMapper mapper, CountDownLatch latch) {
        if (Objects.isNull(instance)) {
            instance = new WebSocketClientListener(handler, mapper, latch);
        }

        return instance;
    }

    private boolean isSubscriptionStatusMessage(final String message) {
        return message.startsWith("{\"channel");
    }

    private boolean isTickerMessage(final String message) {
        return message.startsWith("[");
    }
}
