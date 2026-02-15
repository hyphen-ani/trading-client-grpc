package com.anirudh.trading_client.service;

import com.anirudh.grpc.StockRequest;
import com.anirudh.grpc.StockResponse;
import com.anirudh.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
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


}
