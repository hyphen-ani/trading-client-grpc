package com.anirudh.trading_client.service;

import com.anirudh.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub serviceBlockingStub;

    public StockResponse getStockPrice(String stockSymbol){
        StockRequest request = StockRequest.newBuilder()
                        .setStockSymbol(stockSymbol).build();

        return serviceBlockingStub.getStockPrice(request);
    }

    @GrpcClient("subscribeStock")
    private StockTradingServiceGrpc.StockTradingServiceStub stockTradingServiceStub;

    public void subscribeStockPrice(String symbol){
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(symbol).build();

        stockTradingServiceStub.subscribeStockPrice(request, new StreamObserver<StockResponse>() {
            @Override
            public void onNext(StockResponse stockResponse) {
                System.out.println(
                        "Stock Price Update: " + stockResponse.getStockSymbol() +
                        "Price : " + stockResponse.getPrice() +
                        "Time : " + stockResponse.getTimestamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream Completed and Successfull");
            }
        });
    }


    @GrpcClient("bulkOrder")
    private StockTradingServiceGrpc.StockTradingServiceStub bulkOrderServiceStub;

    public void placeBulkOrders(){

        StreamObserver<OrderSummary> orderResponse = new StreamObserver<OrderSummary>() {
            @Override
            public void onNext(OrderSummary orderSummary) {
                System.out.println("Summary Received");
                System.out.println("Total Orders" + orderSummary.getTotalOrder());
                System.out.println("Total Amount" + orderSummary.getTotalAmount());
                System.out.println("Success Count" + orderSummary.getSuccessCount());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream Completed");
            }
        };

        StreamObserver<StockOrder> stockOrderRequestObserver = bulkOrderServiceStub.bulkStockOrder(orderResponse);

        // Send multiple stream of stock order request

        try{
            stockOrderRequestObserver.onNext(StockOrder.newBuilder()
                            .setId("1")
                            .setStockSymbol("APPL")
                            .setOrderType("BUY")
                            .setQuantity(3)
                            .setPrice(4000)
                            .build());

            stockOrderRequestObserver.onNext(StockOrder.newBuilder()
                    .setId("2")
                    .setStockSymbol("GOOGL")
                    .setOrderType("BUY")
                    .setQuantity(2)
                    .setPrice(10000)
                    .build());

            stockOrderRequestObserver.onCompleted();


        } catch (RuntimeException e) {
            stockOrderRequestObserver.onError(e);
        }
    }


}
