package com.apec_finance.trading.service;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.config.FormFeignEncoderConfig;
import com.apec_finance.trading.model.CreateCashTransaction;
import com.apec_finance.trading.model.InvestorCashBalance;
import com.apec_finance.trading.model.UpdateCashBalance;
import com.apec_finance.trading.model.order.VerifyCashTransaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "cash-client", url = "${spring.feign.client.config.cash-client.url}", configuration = FormFeignEncoderConfig.class)
public interface CashClient {
    @GetMapping("/investor-cash-balance/get")
    ResponseBuilder<InvestorCashBalance> getBalance(@RequestHeader("Authorization") String token);

    @PostMapping("/investor-cash-balance/update")
    ResponseBuilder<Void> updateCashBalance(@RequestHeader("Authorization") String token, @RequestBody UpdateCashBalance updateCashBalance);

    @PostMapping("/investor-cash-transaction/create")
    ResponseBuilder<Void> createCashTransaction(@RequestHeader("Authorization") String token, @RequestBody CreateCashTransaction createCashTransaction);

    @PostMapping("/investor-cash-transaction/verify")
    ResponseBuilder<Void> verifyCashTransaction(@RequestHeader("Authorization") String token, @RequestBody VerifyCashTransaction verifyCashTransaction);
}
