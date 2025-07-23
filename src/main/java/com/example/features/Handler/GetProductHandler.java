package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;
import com.example.features.Domain.Product;
import com.example.features.Service.ProductService;
import com.example.features.exception.GlobalExceptionHandler;
import com.google.gson.Gson;

public class GetProductHandler implements RequestHandler<ApplicationLoadBalancerRequestEvent, ApplicationLoadBalancerResponseEvent> {

    private final ProductService productService = new ProductService();
    private final Gson gson = new Gson();

    @Override
    public ApplicationLoadBalancerResponseEvent handleRequest(ApplicationLoadBalancerRequestEvent request, Context context) {
        try {
            String path = request.getPath();
            context.getLogger().log("Path received: " + path);

            String[] segments = path.split("/");
            if (segments.length < 3) {
                ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
                response.setStatusCode(400);
                response.setStatusDescription("400 Bad Request");
                response.setBody("{\"error\": \"Invalid path. Product ID is missing.\"}");
                response.setIsBase64Encoded(false);
                return response;
            }

            int productId = Integer.parseInt(segments[2]);
            Product product = productService.getProductById(productId);

            if (product == null) {
                ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
                response.setStatusCode(404);
                response.setStatusDescription("404 Not Found");
                response.setBody("{\"error\": \"Product not found with ID: " + productId + "\"}");
                response.setIsBase64Encoded(false);
                return response;
            }

            ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
            response.setStatusCode(200);
            response.setStatusDescription("200 OK");
            response.setBody(gson.toJson(product));
            response.setIsBase64Encoded(false);
            return response;

        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e, context);
        }
    }
}
