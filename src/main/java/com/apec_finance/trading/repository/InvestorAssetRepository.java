package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestorAssetRepository extends JpaRepository<InvestorAssetEntity, Long> {
    List<InvestorAssetEntity> findByInvestorIdAndStatusAndDeleted(Long investorId, Integer status, Integer deleted);
}
