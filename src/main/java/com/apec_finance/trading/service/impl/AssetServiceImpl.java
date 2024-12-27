package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.mapper.InvestorAssetMapper;
import com.apec_finance.trading.model.asset.Asset;
import com.apec_finance.trading.repository.InvestorAssetRepository;
import com.apec_finance.trading.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final InvestorAssetRepository investorAssetRepository;
    private final InvestorAssetMapper investorAssetMapper;
    @Override
    public List<Asset> getListAsset(Long investorId) {
        List<InvestorAssetEntity> assets = investorAssetRepository.findByInvestorIdAndStatusAndDeleted(investorId, 1, 0);
        List<Asset> rs = investorAssetMapper.toDTOs(assets);
        return rs;
    }
}
