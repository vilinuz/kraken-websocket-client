package com.nexo.kraken.websocket.exception;

public class UnknownPairException extends RuntimeException {
    public UnknownPairException(String message) {
        super(message);
    }
}
