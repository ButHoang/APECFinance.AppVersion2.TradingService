package com.apec_finance.trading.service;

import com.apec_finance.trading.model.order.ExpectedInterest;
import com.apec_finance.trading.model.order.OrderDepositRQ;
import com.apec_finance.trading.model.Issuer;
import com.apec_finance.trading.model.order.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService {
    List<Issuer> getIssuerAvailableLimits(List<Integer> productId);
    BigDecimal getAvailableLimitByInvestor(Integer productId, LocalDate date);
    Order createDeposit(OrderDepositRQ rq);
    Order createWithDraw(String assetNo, BigDecimal amount);
    ExpectedInterest getExpectInterest(String assetNo, BigDecimal amount);
    Boolean verifyDepositOrder(Long orderId);
    Boolean verifyWithdrawOrder(Long orderId);
    Boolean checkOTP(String otp);
    Map<Long, Integer> getListProductIdByOrderIds(Set<Long> orderIds);
}
