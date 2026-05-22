package com.sample.api_gateway.proxy;

import com.sample.api_gateway.model.GatewayRequestModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Component
public class ProxyClient {

    private final RestClient restClient;

    public ProxyClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public ResponseEntity<byte[]> forward(
            GatewayRequestModel context,
            HttpServletRequest request,
            byte[] body) {
        URI targetUri = buildTargetUri(context, request);

        HttpHeaders headers = copyHeaders(request);
        headers.set("X-Request-ID", context.getRequestId());

        try {
            return restClient
                    .method(HttpMethod.valueOf(context.getMethod()))
                    .uri(targetUri)
                    .headers(targetHeaders -> targetHeaders.addAll(headers))
                    .body(body == null ? new byte[0] : body)
                    .exchange((requestSpec, responseSpec) -> {
                        HttpHeaders responseHeaders = new HttpHeaders();
                        responseHeaders.addAll(responseSpec.getHeaders());
                        responseHeaders.set("X-Request-ID", context.getRequestId());

                        return ResponseEntity.status(responseSpec.getStatusCode())
                                .headers(responseHeaders)
                                .body(readResponseBody(responseSpec.getBody()));
                    });
        } catch (RestClientException  e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-Request-ID", context.getRequestId());

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .headers(responseHeaders)
                    .body(new byte[0]);
        }
    }

    private byte[] readResponseBody(InputStream body) {
        try {
            return body.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read target response body", e);
        }
    }

    private URI buildTargetUri(GatewayRequestModel context, HttpServletRequest request) {
        String target = context.getTargetUri() + context.getForwardPath();
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            target += "?" + queryString;
        }
        return URI.create(target);
    }

    private HttpHeaders copyHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            Collections.list(request.getHeaders(headerName)).forEach(headerValue -> {
                headers.add(headerName, headerValue);
            });
        });

        headers.remove(HttpHeaders.HOST);
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        return headers;
    }

}
