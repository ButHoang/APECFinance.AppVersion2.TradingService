package com.apec_finance.trading.model.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDepositRQ {
    private Integer productId;
    private String productCode;
    private BigDecimal amount;
    private Long sumInvestmentLimit;
    private Long personalInvestmentLimit;
    private String investorAccountNo;
    private Integer orderQuantity;
}
