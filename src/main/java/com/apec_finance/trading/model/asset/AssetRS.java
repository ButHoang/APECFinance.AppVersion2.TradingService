package com.apec_finance.trading.model.asset;

import com.apec_finance.trading.comon.PaginationRS;
import lombok.Data;

import java.util.List;

@Data
public class AssetRS {
    private PaginationRS<Asset> assets;
    private List<Integer> productIds;
}
