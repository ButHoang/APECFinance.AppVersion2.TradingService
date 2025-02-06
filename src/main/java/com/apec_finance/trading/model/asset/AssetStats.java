package com.apec_finance.trading.model.asset;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetStats {
    private BigDecimal totalAssetValue;
    private Long numOfAsset;
}
