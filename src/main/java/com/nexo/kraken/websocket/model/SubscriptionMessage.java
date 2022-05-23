package com.nexo.kraken.websocket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionMessage {

    @JsonProperty("event")
    @Builder.Default
    private String event = "subscribe";

    @JsonProperty("subscription")
    private Subscription subscription;

    @JsonProperty("pair")
    @Builder.Default
    private List<String> pair = List.of("BTC/USD", "ETH/USD");

    public static SubscriptionMessage create(final Optional<String> token) {
        return SubscriptionMessage.builder()
                .subscription(Subscription.builder().token(token.orElse(null)).build())
                .build();
    }
}
