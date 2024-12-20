package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssetInterestScheduleRepository extends JpaRepository<AssetInterestScheduleEntity, Long> {
    AssetInterestScheduleEntity findByInvestorId(Long investorId);
    @Query("SELECT c FROM AssetInterestScheduleEntity c WHERE c.investorId = :investorId AND c.status = :status and c.deleted = 0")
    AssetInterestScheduleEntity findByInvestorIdAndStatus(@Param("investorId") Long investorId, @Param("status") Integer status);
}


