package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.model.asset.Asset;
import com.apec_finance.trading.service.AssetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Override
    public List<Asset> getListAsset(Long investorId) {
        return null;
    }
}
