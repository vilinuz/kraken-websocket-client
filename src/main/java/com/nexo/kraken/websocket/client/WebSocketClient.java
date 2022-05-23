package com.nexo.kraken.websocket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.kraken.websocket.config.properties.KrakenConfigProperties;
import com.nexo.kraken.websocket.handler.WebSocketMessageHandler;
import com.nexo.kraken.websocket.listener.WebSocketClientListener;
import com.nexo.kraken.websocket.utils.SubscriptionMessageCreator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "nexo.kraken.websocket.profile", havingValue = "prod")
public class WebSocketClient {
    private final KrakenConfigProperties krakenConfigProperties;
    private final SubscriptionMessageCreator subscriptionMessageCreator;
    private final WebSocketMessageHandler webSocketMessageHandler;
    private final ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    @SneakyThrows
    public void subscribe() {
        CountDownLatch latch = new CountDownLatch(1);

        WebSocket ws = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(
                        URI.create(krakenConfigProperties.getWss().getUri()),
                        WebSocketClientListener.create(webSocketMessageHandler, objectMapper, latch))
                .orTimeout(krakenConfigProperties.getConnection().getConnectionTimeout(), TimeUnit.MILLISECONDS).join();

        String jsonMessage = subscriptionMessageCreator.createJsonMessage();
        log.info("Sending subscription message to Kraken: \n{}", jsonMessage);
        ws.sendText(jsonMessage, true);
        latch.await();
        log.info("Done");
    }
}
