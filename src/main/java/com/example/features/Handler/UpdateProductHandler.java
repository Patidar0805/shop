package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;
import com.example.features.Domain.Product;
import com.example.features.Service.ProductService;
import com.example.features.exception.GlobalExceptionHandler;
import com.google.gson.Gson;

import java.util.Map;

public class UpdateProductHandler implements RequestHandler<ApplicationLoadBalancerRequestEvent, ApplicationLoadBalancerResponseEvent> {

    private final ProductService productService = new ProductService();
    private final Gson gson = new Gson();

    @Override
    public ApplicationLoadBalancerResponseEvent handleRequest(ApplicationLoadBalancerRequestEvent request, Context context) {
        try {

            Product input = gson.fromJson(request.getBody(), Product.class);

            productService.updateProduct(input);

            String responseJson = gson.toJson(Map.of("message", "Product updated successfully"));


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
