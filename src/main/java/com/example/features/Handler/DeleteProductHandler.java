package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;
import com.example.features.Service.ProductService;
import com.example.features.exception.GlobalExceptionHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DeleteProductHandler implements RequestHandler<ApplicationLoadBalancerRequestEvent, ApplicationLoadBalancerResponseEvent> {

    private final ProductService productService = new ProductService();
    private final Gson gson = new Gson();

    @Override
    public ApplicationLoadBalancerResponseEvent handleRequest(ApplicationLoadBalancerRequestEvent request, Context context) {
        try {

            JsonObject jsonBody = gson.fromJson(request.getBody(), JsonObject.class);

            if (!jsonBody.has("id") || !jsonBody.get("id").isJsonPrimitive()) {
                throw new IllegalArgumentException("Missing or invalid 'id' in request body.");
            }

            int productId = jsonBody.get("id").getAsInt();

            productService.deleteProduct(productId);


            ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
            response.setStatusCode(200);
            response.setStatusDescription("200 OK");
            response.setIsBase64Encoded(false);
            response.setBody("{\"message\": \"Product deleted successfully (ID: " + productId + ")\"}");

            return response;

        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e, context);
        }
    }
}
