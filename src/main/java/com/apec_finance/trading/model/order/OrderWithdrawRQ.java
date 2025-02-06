package com.apec_finance.trading.model.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderWithdrawRQ {
    private String assetNo;
    private BigDecimal amount;
}
