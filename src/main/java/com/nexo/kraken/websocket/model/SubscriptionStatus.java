package com.nexo.kraken.websocket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscriptionStatus {
    @JsonProperty("channelID")
    private String channelID;

    @JsonProperty("channelName")
    private String channelName;

    @JsonProperty("pair")
    private String pair;

    @JsonProperty("event")
    @Builder.Default
    private String event = "subscriptionStatus";

    @JsonProperty("status")
    private String status = "subscribed";

    @JsonProperty("subscription")
    private Subscription subscription;
}
