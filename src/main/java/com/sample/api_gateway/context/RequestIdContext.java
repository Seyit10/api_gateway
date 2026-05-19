package com.sample.api_gateway.context;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

public class RequestIdContext {
    
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    private RequestIdContext() {}

    public static String getOrCreate(HttpServletRequest request){
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return requestId;
    }
}
