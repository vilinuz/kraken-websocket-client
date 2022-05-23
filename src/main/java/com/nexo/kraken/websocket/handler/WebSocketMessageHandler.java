package com.nexo.kraken.websocket.handler;

import com.nexo.kraken.websocket.model.TickerResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Objects;

import static com.nexo.kraken.websocket.utils.StringUtils.*;

@Component
@Slf4j
/**
 * This class parses the Kraken ticker response into local dto {@code TickerResponse}
 * Expected sample response:
 *
 * [
 *   0,
 *   {
 *     "a": [
 *       "5525.40000",
 *       1,
 *       "1.000"
 *     ],
 *     "b": [
 *       "5525.10000",
 *       1,
 *       "1.000"
 *     ],
 *     "c": [
 *       "5525.10000",
 *       "0.00398963"
 *     ],
 *     "h": [
 *       "5783.00000",
 *       "5783.00000"
 *     ],
 *     "l": [
 *       "5505.00000",
 *       "5505.00000"
 *     ],
 *     "o": [
 *       "5760.70000",
 *       "5763.40000"
 *     ],
 *     "p": [
 *       "5631.44067",
 *       "5653.78939"
 *     ],
 *     "t": [
 *       11493,
 *       16267
 *     ],
 *     "v": [
 *       "2634.11501494",
 *       "3591.17907851"
 *     ]
 *   },
 *   "ticker",
 *   "BTC/USD"
 * ]
 */
public class WebSocketMessageHandler {

    private static WebSocketMessageHandler instance;
    private final TickerResponse tickerResponse;

    private WebSocketMessageHandler() {
        tickerResponse = TickerResponse.create();
    }

    @SneakyThrows
    public TickerResponse parse(String message) {
        log.info("Handling message \n {}", message);

        String normalizedMessage = normalizeMessage(message);
        String ask = extractAsk(normalizedMessage);
        String bid = extractBid(normalizedMessage);
        String pair = extractPair(normalizedMessage);

        tickerResponse.getAsks().put(pair, ask);
        tickerResponse.getBids().put(pair, bid);
        tickerResponse.setDateTime(ZonedDateTime.now());
        tickerResponse.setPair(pair);
        return tickerResponse;
    }

    private String normalizeMessage(String message) {
        return message
                .replaceAll("[\n\r]", "")
                .replaceAll("[\n]", "")
                .replaceAll("\\s", "");
    }

    public static WebSocketMessageHandler instance() {
        if (Objects.isNull(instance)) {
            instance = new WebSocketMessageHandler();
        }

        return instance;
    }
}
