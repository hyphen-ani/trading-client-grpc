package com.anirudh.trading_client.service;

import com.anirudh.grpc.StockRequest;
import com.anirudh.grpc.StockResponse;
import com.anirudh.grpc.StockTradingServiceGrpc;
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


}
