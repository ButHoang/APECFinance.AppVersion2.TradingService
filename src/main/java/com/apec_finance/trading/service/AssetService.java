package com.apec_finance.trading.service;

import com.apec_finance.trading.model.asset.*;

import java.util.List;

public interface AssetService {
    AssetRS getListAsset(SearchAssetRQ searchAssetRQ, Long investorId);
    AssetOrderInfo getAssetAndOrderInfo(String assetNo);
    AssetProduct getAssetAndProductInfo(Long investorId, List<Integer> productIds);
    AssetStats getAssetStats(Long investorId, Integer status);
}
