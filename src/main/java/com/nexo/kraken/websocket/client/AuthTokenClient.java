package com.nexo.kraken.websocket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.kraken.websocket.config.properties.KrakenConfigProperties;
import com.nexo.kraken.websocket.exception.AuthTokenException;
import com.nexo.kraken.websocket.model.AuthToken;
import com.nexo.kraken.websocket.model.AuthTokenResponse;
import com.nexo.kraken.websocket.model.SignatureDetails;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;

import static java.lang.String.format;
import static java.lang.String.valueOf;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * This class obtains authentication token from Kraken using API Key and Secret.
 * Presently it's not used, as we don't need to access any private endpoints.
 */
public class AuthTokenClient {
    private final KrakenConfigProperties krakenConfigProperties;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public Optional<String> getToken(String endpointName, String inputParameters, String apiPublicKey, String apiPrivateKey) {
        String baseDomain = krakenConfigProperties.getApi().getBaseDomain();
        String privatePath = krakenConfigProperties.getApi().getPrivatePath();
        String nonce = valueOf(System.currentTimeMillis());
        String requestParams = "nonce=" + nonce + "&" + inputParameters;

        SignatureDetails signatureDetails = SignatureDetails.builder()
                .apiPath(privatePath)
                .requestParams(requestParams)
                .apiPrivateKey(apiPrivateKey)
                .endPointName(endpointName)
                .nonce(nonce)
                .build();

        String signature = createAuthenticationSignature(signatureDetails);
        String fullEndpoint = baseDomain + privatePath + endpointName + "?" + inputParameters;
        HttpURLConnection connection = createConnection(fullEndpoint, apiPublicKey, signature);

        return Optional.ofNullable(requestToken(connection, requestParams).getToken());
    }

    private AuthToken requestToken(HttpURLConnection connection, String requestParams) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(requestParams);
        outputStream.flush();
        outputStream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        AuthTokenResponse tokenResponse = objectMapper.readValue(response.toString(), AuthTokenResponse.class);

        return Optional.ofNullable(tokenResponse)
                .map(AuthTokenResponse::getResult)
                .orElseThrow(() -> new AuthTokenException("Failed to deserialize the authentication token response."));
    }

    @SneakyThrows
    private HttpURLConnection createConnection(String fullEndpoint, String apiPublicKey, String signature) {
        HttpsURLConnection connection = null;
        URL apiUrl = new URL(fullEndpoint);
        connection = (HttpsURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("API-Key", apiPublicKey);
        connection.setRequestProperty("API-Sign", signature);
        connection.setDoOutput(true);
        return connection;
    }

    @SneakyThrows
    private String createAuthenticationSignature(SignatureDetails details) {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(format("%s%s", details.getNonce(), details.getRequestParams()).getBytes());
        byte[] sha256Hash = md.digest();

        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(new SecretKeySpec(Base64.getDecoder().decode(details.getApiPrivateKey().getBytes()), "HmacSHA512"));
        mac.update(format("%s%s", details.getApiPath(), details.getEndPointName()).getBytes());

        return Base64.getEncoder().encodeToString(mac.doFinal(sha256Hash));
    }
}
