package com.nexo.kraken.websocket.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KrakenMessage {
    private long maxSizeInBytes;
}
