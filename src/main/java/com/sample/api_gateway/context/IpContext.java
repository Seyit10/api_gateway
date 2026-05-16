package com.sample.api_gateway.context;

import jakarta.servlet.http.HttpServletRequest;

public class IpContext {
    
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_REAL_IP = "X-Real-IP";
    
    private IpContext() {
    
    }

    public static String resolveClientIp(HttpServletRequest request){
        String forwardedFor = request.getHeader(X_FORWARDED_FOR);
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader(X_REAL_IP);

        if(realIp != null && !realIp.isBlank()){
            return realIp;
        }

        return request.getRemoteAddr();
    }

}
