package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.order.OrderDepositRQ;
import com.apec_finance.trading.model.Issuer;
import com.apec_finance.trading.model.order.Order;
import com.apec_finance.trading.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/available-limits/by-product")
    public ResponseBuilder<List<Issuer>> getAvailableLimitByProducts(@RequestParam List<Integer> productIds) {
        List<Issuer> rs = orderService.getIssuerAvailableLimits(productIds);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/available-limits/by-investor")
    public ResponseBuilder<BigDecimal> getAvailableLimitByInvestor(@RequestParam Integer productId) {
        BigDecimal rs = orderService.getAvailableLimitByInvestor(productId);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/accumulate")
    public ResponseBuilder<Order> accumulate(@RequestBody OrderDepositRQ rq) {
        Order rs = orderService.createDeposite(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
