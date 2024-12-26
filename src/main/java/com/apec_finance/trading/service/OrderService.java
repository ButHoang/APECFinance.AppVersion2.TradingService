package com.apec_finance.trading.service;

import com.apec_finance.trading.model.order.OrderDepositRQ;
import com.apec_finance.trading.model.Issuer;
import com.apec_finance.trading.model.order.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<Issuer> getIssuerAvailableLimits(List<Integer> productId);
    BigDecimal getAvailableLimitByInvestor(Integer productId);

    Order createDeposite(OrderDepositRQ rq);
}
