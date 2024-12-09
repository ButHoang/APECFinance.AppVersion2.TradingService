package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.entity.AssetEntity;
import com.apec_finance.trading.exception.validate.ValidationException;
import com.apec_finance.trading.mapper.AssetMapper;
import com.apec_finance.trading.model.Asset;
import com.apec_finance.trading.repository.AssetRepository;
import com.apec_finance.trading.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetMapper assetMapper;
    private final AssetRepository assetRepository;
    @Override
    public Asset getTotalAsset(Long accountId) {
        AssetEntity asset = assetRepository.findById(accountId)
                .orElseThrow(() -> new ValidationException("Username is already taken"));
        Asset rs = assetMapper.getDTO(asset);
        return rs;
    }
}
