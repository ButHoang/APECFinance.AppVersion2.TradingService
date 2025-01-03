package com.apec_finance.trading.service;

import com.apec_finance.trading.config.FormFeignEncoderConfig;
import com.apec_finance.trading.model.order.AppProduct;
import com.apec_finance.trading.model.order.AppStockRS;
import com.apec_finance.trading.model.TransactionRange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(name = "app-client", url = "${spring.feign.client.config.app-client.url}", configuration = FormFeignEncoderConfig.class)
public interface AppClient {
    @GetMapping("/app-api/get_transaction_range")
    List<TransactionRange> getTransactionRange();

//    @GetMapping("/items/stocks")
//    AppStockRS getStockRs(@RequestParam("filter[id][_eq]") Integer filter, @RequestParam String fields);

    @GetMapping("/items/product")
    AppStockRS getStockRs(@RequestParam("filter[id][_eq]") Integer filter, @RequestParam String fields);
}
