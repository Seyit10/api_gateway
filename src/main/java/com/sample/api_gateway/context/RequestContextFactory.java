package com.sample.api_gateway.context;

import com.sample.api_gateway.model.GatewayRequestModel;
import com.sample.api_gateway.route.GatewayRoute;
import com.sample.api_gateway.route.RouteMatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class RequestContextFactory {

    private final RouteMatcher routeMatcher;

    public RequestContextFactory(RouteMatcher routeMatcher) {
        this.routeMatcher = routeMatcher;
    }

    public Optional<GatewayRequestModel> create(HttpServletRequest request) {
        String originalPath = request.getRequestURI();

        return routeMatcher.match(originalPath)
                .map(route -> buildContext(request, originalPath, route));
    }

    private GatewayRequestModel buildContext(
            HttpServletRequest request,
            String originalPath,
            GatewayRoute route
    ) {
        GatewayRequestModel context = new GatewayRequestModel();

        context.setRequestId(RequestIdContext.getOrCreate(request));
        context.setMethod(request.getMethod());
        context.setOriginalPath(originalPath);
        context.setForwardPath(routeMatcher.buildForwardPath(route, originalPath));
        context.setTargetUri(route.getTargetUri());
        context.setClientIp(IpContext.resolveClientIp(request));
        context.setMatchedRoute(route);
        context.setStartedAt(Instant.now());

        return context;
    }
}