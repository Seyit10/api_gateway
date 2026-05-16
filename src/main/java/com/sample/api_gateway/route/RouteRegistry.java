package com.sample.api_gateway.route;

import java.util.List;

import com.sample.api_gateway.config.GatewayProperties;
import org.springframework.stereotype.Component;

@Component
public class RouteRegistry {
    private final GatewayProperties gatewayProperties;
    
    public RouteRegistry(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    public List<GatewayRoute> getRoutes() {
        return gatewayProperties.getRoutes();
    }
    
}
