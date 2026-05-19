package com.sample.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.api_gateway.context.RequestContextFactory;
import com.sample.api_gateway.proxy.ProxyClient;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GatewayController {

    private final RequestContextFactory requestContextFactory;
    private final ProxyClient proxyClient;

    public GatewayController(RequestContextFactory requestContextFactory, ProxyClient proxyClient) {
        this.requestContextFactory = requestContextFactory;
        this.proxyClient = proxyClient;
    }

    @RequestMapping("/gw/**")
    public ResponseEntity<?> handleRequest(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        return requestContextFactory.create(request)
                .map(context -> proxyClient.forward(context, request, body))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
}
