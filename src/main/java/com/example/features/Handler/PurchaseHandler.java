package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;
import com.example.features.Domain.Purchase;
import com.example.features.Service.PurchaseService;
import com.example.features.exception.GlobalExceptionHandler;
import com.google.gson.Gson;

import java.util.Map;

public class PurchaseHandler implements RequestHandler<ApplicationLoadBalancerRequestEvent, ApplicationLoadBalancerResponseEvent> {

    private final PurchaseService purchaseService = new PurchaseService();
    private final Gson gson = new Gson();

    @Override
    public ApplicationLoadBalancerResponseEvent handleRequest(ApplicationLoadBalancerRequestEvent request, Context context) {
        try {
            // Deserialize JSON body to Purchase object
            Purchase input = gson.fromJson(request.getBody(), Purchase.class);


            purchaseService.processPurchase(input);


            String message = "Purchase completed for " + input.getCustomerName();
            String responseJson = gson.toJson(Map.of("message", message));


            ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
            response.setStatusCode(200);
            response.setStatusDescription("200 OK");
            response.setBody(responseJson);
            response.setIsBase64Encoded(false);

            return response;

        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e, context);
        }
    }
}
