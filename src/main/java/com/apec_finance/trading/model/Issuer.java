package com.apec_finance.trading.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Issuer {
    private Integer productId;
    private BigDecimal availableLimit;
}
