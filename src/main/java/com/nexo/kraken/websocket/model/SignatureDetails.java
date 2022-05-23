package com.nexo.kraken.websocket.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignatureDetails {
    private String apiPrivateKey;
    private String apiPath;
    private String endPointName;
    private String nonce;
    private String requestParams;
}
