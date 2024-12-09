package com.apec_finance.trading.service;

import com.apec_finance.trading.model.Asset;

public interface AssetService {
    Asset getTotalAsset(Long accountId);
}
