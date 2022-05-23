package com.nexo.kraken.websocket.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.kraken.websocket.model.SubscriptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscriptionMessageCreator {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String createJsonMessage() {
        return createJsonMessage(Optional.empty());
    }

    @SneakyThrows
    public String createJsonMessage(final Optional<String> token) {
        return objectMapper.writeValueAsString(SubscriptionMessage.create(token));
    }
}
