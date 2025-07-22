package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class MainHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
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
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody("Unsupported HTTP method: " + httpMethod);
        }
    }
}
