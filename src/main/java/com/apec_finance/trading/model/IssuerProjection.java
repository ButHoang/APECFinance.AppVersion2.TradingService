package com.apec_finance.trading.model;

import java.math.BigDecimal;

public interface IssuerProjection {
    Integer getProductId();
    BigDecimal getAvailableLimit();
}

