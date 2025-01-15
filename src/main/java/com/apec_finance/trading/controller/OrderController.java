package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.order.ExpectedInterest;
import com.apec_finance.trading.model.order.OrderDepositRQ;
import com.apec_finance.trading.model.Issuer;
import com.apec_finance.trading.model.order.Order;
import com.apec_finance.trading.model.order.OrderWithdrawRQ;
import com.apec_finance.trading.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @PostMapping("/deposit")
    public ResponseBuilder<Order> accumulate(@RequestBody OrderDepositRQ rq) {
        Order rs = orderService.createDeposit(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/withdraw")
    public ResponseBuilder<Order> createWithdraw(@RequestBody OrderWithdrawRQ rq) {
        Order rs = orderService.createWithDraw(rq.getAssetNo(), rq.getAmount());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/expected-interest")
    public ResponseBuilder<ExpectedInterest> getExpectedInterest(@RequestParam String assetNo, @RequestParam BigDecimal amount) {
        var rs = orderService.getExpectInterest(assetNo, amount);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/deposit/verify")
    public ResponseBuilder<Boolean> verifyDepositOrder(@RequestBody Long orderId) {
        var rs = orderService.verifyDepositOrder(orderId);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/otp")
    public ResponseBuilder<Boolean> checkOTP(@RequestParam String otp) {
        var rs = orderService.checkOTP(otp);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
    @GetMapping("/product-id")
    public ResponseBuilder<Map<Long, Integer>> getListProductId(@RequestParam Set<Long> orderIds) {
        var rs = orderService.getListProductIdByOrderIds(orderIds);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
