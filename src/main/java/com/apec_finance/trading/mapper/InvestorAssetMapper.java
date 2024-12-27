package com.apec_finance.trading.mapper;

import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.model.asset.Asset;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface InvestorAssetMapper {
    List<Asset> toDTOs(List<InvestorAssetEntity> assetEntities);
}
