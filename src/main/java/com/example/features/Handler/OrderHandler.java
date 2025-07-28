package com.example.features.Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;
import com.example.features.Domain.order;
import com.example.features.Service.OrderService;
import com.example.features.exception.GlobalExceptionHandler;
import com.google.gson.Gson;

import java.util.Map;

public class OrderHandler implements RequestHandler<ApplicationLoadBalancerRequestEvent, ApplicationLoadBalancerResponseEvent> {

    private final OrderService orderService = new OrderService();
    private final Gson gson = new Gson();

    @Override
    public ApplicationLoadBalancerResponseEvent handleRequest(ApplicationLoadBalancerRequestEvent request, Context context) {
        try {
            String method = request.getHttpMethod();
            String path = request.getPath();
            String body = request.getBody();
            Map<String, String> queryParams = request.getQueryStringParameters();

            switch (method) {
                case "POST" -> {

                    OrderRequest orderRequest = gson.fromJson(body, OrderRequest.class);
                    int orderId = orderService.placeFullOrder(orderRequest.nickname, orderRequest.products);
                    order ord = orderService.getOrderByNickname(orderRequest.nickname);
                    return successResponse(201, gson.toJson(ord));
                }

                case "GET" -> {

                    String nickname = queryParams != null ? queryParams.get("nickname") : null;
                    if (nickname == null) {
                        return errorResponse(400, "Missing query parameter: nickname");
                    }
                    order ord = orderService.getOrderByNickname(nickname);
                    if (ord == null) {
                        return errorResponse(404, "Order not found");
                    }
                    return successResponse(200, gson.toJson(ord));
                }

                case "DELETE" -> {

                    String nickname = queryParams != null ? queryParams.get("nickname") : null;
                    if (nickname == null) {
                        return errorResponse(400, "Missing query parameter: nickname");
                    }
                    boolean deleted = orderService.deleteOrderByNickname(nickname);
                    return successResponse(200, "{\"deleted\": " + deleted + "}");
                }

                case "PUT" -> {

                    OrderRequest orderRequest = gson.fromJson(body, OrderRequest.class);
                    boolean updated = orderService.updateOrder(orderRequest.nickname, orderRequest.products);
                    if (!updated) {
                        return errorResponse(400, "Update failed");
                    }
                    order ord = orderService.getOrderByNickname(orderRequest.nickname);
                    return successResponse(200, gson.toJson(ord));
                }

                default -> {
                    return errorResponse(405, "Method Not Allowed");
                }
            }

        } catch (Exception e) {
            return GlobalExceptionHandler.handleException(e, context);
        }
    }

    private ApplicationLoadBalancerResponseEvent successResponse(int code, String body) {
        ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
        response.setStatusCode(code);
        response.setStatusDescription(code + " OK");
        response.setIsBase64Encoded(false);
        response.setBody(body);
        return response;
    }

    private ApplicationLoadBalancerResponseEvent errorResponse(int code, String message) {
        ApplicationLoadBalancerResponseEvent response = new ApplicationLoadBalancerResponseEvent();
        response.setStatusCode(code);
        response.setStatusDescription(code + " Error");
        response.setIsBase64Encoded(false);
        response.setBody("{\"error\": \"" + message + "\"}");
        return response;
    }


    private static class OrderRequest {
        String nickname;
        Map<Integer, Integer> products;
    }
}
