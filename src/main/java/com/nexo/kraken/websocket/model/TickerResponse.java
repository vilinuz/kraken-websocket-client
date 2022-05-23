
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
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode
public class TickerResponse {
    @Builder.Default
    private Multimap<String, String> asks =
            TreeMultimap.create(Comparator.naturalOrder(), Comparator.reverseOrder());
    @Builder.Default
    private Multimap<String, String> bids =
            TreeMultimap.create(Comparator.naturalOrder(), Comparator.reverseOrder());
    private String pair;
    private ZonedDateTime dateTime;

    public static TickerResponse create() {
        return TickerResponse.builder().build();
    }

    private String getBestBid() {
        return getBids().entries().stream()
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");
    }

    private String getBestAsk() {
        return getAsks().entries().stream()
                .map(Map.Entry::getValue)
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

        StringBuilder result = new StringBuilder();

        result.append("<------------------------------------->\n");
        result.append("asks:\n").append("[ ");
        this.getAsks().entries().forEach(entry -> {
            result.append("[ ")
                    .append(entry.getKey())
                    .append(", ")
                    .append(entry.getValue())
                    .append(" ],\n");
        });

        result.delete(result.length() - 2, result.length());
        result.append(" ]\n");


        result.append("best bid: [ " + getBestBid() + " ]\n");
        result.append("best ask: [ " + getBestAsk() +" ]\n");

        result.append("bids:\n").append("[ ");

        this.getBids().entries().forEach(entry -> {
            result.append("[ ")
                    .append(entry.getKey())
                    .append(", ")
                    .append(entry.getValue())
                    .append(" ],\n");
        });

        result.delete(result.length() - 2, result.length());
        result.append(" ]\n");

        result.append(formatDateTime() + "\n");
        result.append(pair + "\n");
        result.append(">-------------------------------------<\n");

        return result.toString();
    }
}
