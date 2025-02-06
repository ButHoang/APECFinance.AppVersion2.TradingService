package com.apec_finance.trading.model.interest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InterestSummary {
    private BigDecimal totalAsset;
    private int numOfAsset;
    private BigDecimal totalReceivedInterestValue;
    private BigDecimal totalExpectedInterestValue;
}
