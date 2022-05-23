package com.nexo.kraken.websocket.utils;

import com.nexo.kraken.websocket.handler.WebSocketMessageHandler;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestUtil {

    @SneakyThrows
    public static String getTickerMessage() {
        InputStream inputStream = WebSocketMessageHandler.class.getResourceAsStream("/kraken-sample-ticker.response");
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}
