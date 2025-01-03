package com.apec_finance.trading.model.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpectedInterest {
    private BigDecimal withdrawalInterestRate;
    private BigDecimal totalInterestReceived;
    private BigDecimal incomeTaxPaid;
    private Long interestDays;
    private BigDecimal actualInterest;
    private BigDecimal personalIncomeTax;
    private BigDecimal remainingInterest;
    private BigDecimal actualReceived;
}
