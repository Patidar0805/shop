package com.example.features.exception;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class GlobalExceptionHandler {

    public static APIGatewayProxyResponseEvent handleException(Exception e, Context context) {
        // Optional: log to CloudWatch
        context.getLogger().log("Exception: " + e.getMessage());

        // Return a proper API Gateway response
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody("{\"error\": \"" + e.getMessage() + "\"}");
    }
}
