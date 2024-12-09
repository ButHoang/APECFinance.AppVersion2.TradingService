package com.apec_finance.trading.mapper;

import com.apec_finance.trading.entity.AssetEntity;
import com.apec_finance.trading.model.Asset;
import org.mapstruct.Mapper;

@Mapper
public interface AssetMapper {
    Asset getDTO(AssetEntity assetEntity);
    AssetEntity getEntity(Asset asset);
}
