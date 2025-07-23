package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;
import com.example.features.Domain.Product;
import com.example.features.Service.ProductService;
import com.example.features.exception.GlobalExceptionHandler;
import com.google.gson.Gson;

public class CreateProductHandler implements RequestHandler<ApplicationLoadBalancerRequestEvent, ApplicationLoadBalancerResponseEvent> {

    private final ProductService productService = new ProductService();
    private final Gson gson = new Gson();

    @Override
    public ApplicationLoadBalancerResponseEvent handleRequest(ApplicationLoadBalancerRequestEvent request, Context context) {
        try {

            Product product = gson.fromJson(request.getBody(), Product.class);

            productService.createProduct(product);

            ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
            response.setStatusCode(201);
            response.setStatusDescription("201 Created");
            response.setIsBase64Encoded(false);
            response.setBody("{\"message\": \"Product created successfully\"}");
            return response;

        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e, context);
        }
    }
}
