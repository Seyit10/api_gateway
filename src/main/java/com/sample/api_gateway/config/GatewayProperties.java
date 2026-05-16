package com.sample.api_gateway.config;

import com.sample.api_gateway.route.GatewayRoute;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {
    private List<GatewayRoute> routes = new ArrayList<>();
}
