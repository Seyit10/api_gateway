package com.sample.api_gateway.route;

import lombok.Data;

@Data
public class GatewayRoute {
    private String id;
    private String pathPrefix;
    private String targetUri;
    private String stripPrefix;
    private boolean requiresAuth;
}