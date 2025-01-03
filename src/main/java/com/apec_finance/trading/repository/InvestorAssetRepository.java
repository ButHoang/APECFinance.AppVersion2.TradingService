package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestorAssetRepository extends JpaRepository<InvestorAssetEntity, Long> {
    List<InvestorAssetEntity> findByInvestorIdAndStatusAndDeleted(Long investorId, Integer status, Integer deleted);

    InvestorAssetEntity findByAssetNoAndStatusAndDeleted(String assetNo, Integer status, Integer deleted);

    @Query("SELECT distinct ia.investorAccountNo FROM InvestorAssetEntity ia WHERE ia.investorId = :investorId AND ia.status = :status AND ia.deleted = :deleted")
    String findInvestorAccountNoByInvestorIdAndStatusAndDeleted(@Param("investorId") Long investorId,
                                                                @Param("status") Integer status,
                                                                @Param("deleted") Integer deleted);
}
