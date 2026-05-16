package com.sample.api_gateway.debug;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import com.sample.api_gateway.route.RouteMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug/routes")
public class RouteDebugController {
    private final RouteMatcher routeMatcher;

    public RouteDebugController(RouteMatcher routeMatcher) {
        this.routeMatcher = routeMatcher;
    }

    @GetMapping("/match")
    public ResponseEntity<?> match(@RequestParam String path){
        return routeMatcher.match(path)
                .map(route -> ResponseEntity.ok(Map.of(
                        "matchedRouteId", route.getId(),
                        "pathPrefix", route.getPathPrefix(),
                        "targetUri", route.getTargetUri(),
                        "forwardPath", routeMatcher.buildForwardPath(route, path),
                        "requiresAuth", route.isRequiresAuth()
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
