package com.example.features.exception;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class GlobalExceptionHandler {

    public static ApplicationLoadBalancerResponseEvent handleException(Exception e, Context context) {
        context.getLogger().log("Error: " + e.getMessage());

        ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
        response.setStatusCode(500);
        response.setStatusDescription("500 Internal Server Error");
        response.setIsBase64Encoded(false);
        response.setBody("{\"error\": \"" + e.getMessage() + "\"}");

        return response;
    }

}
