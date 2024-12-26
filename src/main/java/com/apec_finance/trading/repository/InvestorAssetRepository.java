package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorAssetRepository extends JpaRepository<InvestorAssetEntity, Long> {
    InvestorAssetEntity findByInvestorIdAndStatusAndDeleted(Long investorId, Integer status, Integer deleted);
}
