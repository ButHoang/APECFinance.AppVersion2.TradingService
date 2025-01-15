package com.apec_finance.trading.service;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.model.asset.Asset;
import com.apec_finance.trading.model.asset.AssetRS;
import com.apec_finance.trading.model.asset.SearchAssetRQ;

import java.util.List;

public interface AssetService {
    AssetRS getListAsset(SearchAssetRQ searchAssetRQ, Long investorId);
}
