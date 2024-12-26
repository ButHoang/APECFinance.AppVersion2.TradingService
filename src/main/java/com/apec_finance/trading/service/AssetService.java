package com.apec_finance.trading.service;

import com.apec_finance.trading.model.asset.Asset;

import java.util.List;

public interface AssetService {
    List<Asset> getListAsset(Long investorId);
}
