package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.features.Domain.Purchase;
import com.example.features.Service.PurchaseService;
import com.example.features.exception.GlobalExceptionHandler;
import com.google.gson.Gson;

import java.util.Map;

public class PurchaseHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final PurchaseService purchaseService = new PurchaseService();
    private final Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            // Deserialize JSON body to Purchase object
            Purchase input = gson.fromJson(request.getBody(), Purchase.class);

            // Process the purchase
            purchaseService.processPurchase(input);

            // Create success response
            String message = "Purchase completed for " + input.getCustomerName();
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(gson.toJson(Map.of("message", message)));

        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e, context);
        }
    }
}
