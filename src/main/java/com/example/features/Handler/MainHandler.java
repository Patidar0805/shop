package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;

public class MainHandler implements RequestHandler<ApplicationLoadBalancerRequestEvent, ApplicationLoadBalancerResponseEvent> {

    @Override
    public ApplicationLoadBalancerResponseEvent handleRequest(ApplicationLoadBalancerRequestEvent request, Context context) {
        String httpMethod = request.getHttpMethod();
        String path = request.getPath();

        switch (httpMethod.toUpperCase()) {
            case "POST":
                return new CreateProductHandler().handleRequest(request, context);
            case "DELETE":
                return new DeleteProductHandler().handleRequest(request, context);
            case "GET":
                if (path != null && path.matches("/products/\\d+")) {
                    return new GetProductHandler().handleRequest(request, context);
                } else {
                    return new GetAllProductsHandler().handleRequest(request, context);
                }
            case "PUT":
                return new UpdateProductHandler().handleRequest(request, context);
            default:
                ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
                response.setStatusCode(400);
                response.setStatusDescription("400 Bad Request");
                response.setBody("Unsupported HTTP method: " + httpMethod);
                response.setIsBase64Encoded(false);
                return response;
        }
    }
}
