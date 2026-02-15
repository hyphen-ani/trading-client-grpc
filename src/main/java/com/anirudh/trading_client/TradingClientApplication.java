package com.anirudh.trading_client;

import com.anirudh.trading_client.service.StockClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradingClientApplication implements CommandLineRunner {

	@Autowired
	private StockClientService stockClientService;

	public static void main(String[] args) {
		SpringApplication.run(TradingClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		System.out.println("GRPC Client Response :" + stockClientService.getStockPrice("INTC"));
//		stockClientService.subscribeStockPrice("APPL");
		stockClientService.placeBulkOrders();
	}
}
