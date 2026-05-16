package com.sample.api_gateway.route;

import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.Optional;

@Component
public class RouteMatcher {
    private final RouteRegistry routeRegistry;

    public RouteMatcher(RouteRegistry routeRegistry) {
        this.routeRegistry = routeRegistry;
    }

    public Optional<GatewayRoute> match(String requestPath){
        return routeRegistry.getRoutes().stream()
                .filter(route -> requestPath.startsWith(route.getPathPrefix()))
                .max(Comparator.comparingInt(route -> route.getPathPrefix().length()));
    }

    public String buildForwardPath(GatewayRoute route, String requestPath) {
        String stripPrefix = route.getStripPrefix();

        if(stripPrefix == null || stripPrefix.isBlank()) {
            return requestPath;
        }

        if(!requestPath.startsWith(stripPrefix)) {
            throw new IllegalArgumentException("Request path does not start with route's path prefix");
        }

        String forwardPath = requestPath.substring(stripPrefix.length());

        if(forwardPath.isBlank()) {
            forwardPath = "/";
        }

        return forwardPath;
    }
}
