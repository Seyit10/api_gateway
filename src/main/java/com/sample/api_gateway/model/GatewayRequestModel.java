package com.sample.api_gateway.model;

import com.sample.api_gateway.route.GatewayRoute;
import lombok.Data;

import java.time.Instant;

@Data
public class GatewayRequestModel {
    private String requestId;
    private String method;
    private String originalPath;
    private String forwardPath;
    private String targetUri;
    private String clientIp;
    private GatewayRoute matchedRoute;
    private Instant startedAt;
}
