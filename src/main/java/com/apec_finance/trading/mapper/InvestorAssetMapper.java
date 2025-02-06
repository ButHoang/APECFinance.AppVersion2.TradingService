package com.apec_finance.trading.mapper;

import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.model.asset.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper
public interface InvestorAssetMapper {
    List<Asset> toDTOs(List<InvestorAssetEntity> assetEntities);
}