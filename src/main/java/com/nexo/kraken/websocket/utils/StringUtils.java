package com.nexo.kraken.websocket.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String extractPair(final String response) {
        int startIndex = response.lastIndexOf(",\"");
        int endIndex = response.lastIndexOf("\"]");
        return response.substring(startIndex + 2, endIndex);
    }

    public static String extractBid(final String response) {
        int bidsIndex = response.indexOf("\"b\":");
        String bidPartBeginning = response.substring(bidsIndex + 2);
        int newBidsIndex = bidPartBeginning.indexOf("\"b\":");
        String bidPart = bidPartBeginning
                .substring(newBidsIndex + 4, bidPartBeginning.indexOf("]"))
                .replace("\"", "");

        return bidPart.split(",")[0] + "," + bidPart.split(",")[0];
    }

    public static String extractAsk(final String response) {
        int asksIndex = response.indexOf("\"a\":");
        String askPart = response
                .substring(asksIndex + 5, response.indexOf("]"))
                .replace("\"", "");

        return askPart.split(",")[0] + "," + askPart.split(",")[0];
    }

    public static boolean isBtcUsdResponse(final String response) {
        return response.contains("BTC/USD");
    }

    public boolean isEthUsdResponse(final String response) {
        return response.contains("ETH/USD");
    }

}
