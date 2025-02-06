package com.apec_finance.trading.model.asset;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetProduct {
    private BigDecimal asavingValue;
    private BigDecimal cashUpValue;
    private BigDecimal waitingValue;
}
