
package com.nexo.kraken.websocket.model;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Comparator;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode
public class TickerResponse {
    private static final List<String> PAIRS = List.of("BTC/USD", "ETH/USD");

    @Builder.Default
    private Multimap<String, String> asks =
            TreeMultimap.create(Comparator.naturalOrder(), Comparator.reverseOrder());
    @Builder.Default
    private Multimap<String, String> bids =
            TreeMultimap.create(Comparator.naturalOrder(), Comparator.reverseOrder());
    private ZonedDateTime dateTime;

    public static TickerResponse create() {
        return TickerResponse.builder().build();
    }

    private String getBestBid(String pair) {
        return bids.get(pair).stream()
                .findFirst()
                .orElse("");
    }

    private String getBestAsk(String pair) {
        return asks.get(pair).stream()
                .findFirst()
                .orElse("");
    }

    private String formatDateTime() {
        DateTimeFormatter formatter =
                new DateTimeFormatterBuilder()
                        .appendInstant()
                        .toFormatter();
        return dateTime.format(formatter);
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        PAIRS.forEach(pair -> {
            appendHeader(builder);
            appendAsks(builder, pair);
            appendBests(builder, pair);
            appendBids(builder, pair);
            appendFooter(builder, pair);
        });

        return builder.toString();
    }

    private void appendHeader(StringBuilder builder) {
        builder.append("<------------------------------------->\n");
    }

    private void appendFooter(StringBuilder builder, String pair) {
        builder.append(formatDateTime()).append("\n");
        builder.append(pair).append("\n");
        builder.append(">-------------------------------------<\n");
    }

    private void appendAsks(StringBuilder builder, String pair) {
        builder.append("asks:\n").append("[ ");
        asks.get(pair).forEach(ask ->
                builder.append("[ ")
                        .append(ask)
                        .append(" ],\n"));

        builder.delete(builder.length() - 2, builder.length());
        builder.append(" ]\n");
    }

    private void appendBids(StringBuilder builder, String pair) {
        builder.append("bids:\n").append("[ ");

        bids.get(pair).forEach(bid ->
                builder.append("[ ")
                        .append(bid)
                        .append(" ],\n"));

        builder.delete(builder.length() - 2, builder.length());
        builder.append(" ]\n");
    }

    private void appendBests(StringBuilder builder, String pair) {
        builder.append("best bid: [ ").append(getBestBid(pair)).append(" ]\n");
        builder.append("best ask: [ ").append(getBestAsk(pair)).append(" ]\n");
    }
}
