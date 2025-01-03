package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AssetInterestScheduleRepository extends JpaRepository<AssetInterestScheduleEntity, Long> {
    AssetInterestScheduleEntity findByInvestorId(Long investorId);
    List<AssetInterestScheduleEntity> findByAssetIdAndInterestDateGreaterThan(Long assetId, LocalDate currentDate);

    @Query("SELECT c FROM AssetInterestScheduleEntity c WHERE c.investorId = :investorId AND c.status = :status and c.deleted = 0")
    AssetInterestScheduleEntity findByInvestorIdAndStatus(@Param("investorId") Long investorId, @Param("status") Integer status);

    List<AssetInterestScheduleEntity> findByAssetNoAndStatusAndDeleted(String assetNo, Integer status, Integer deleted);

    @Query("SELECT a FROM AssetInterestScheduleEntity a " +
            "WHERE a.assetNo = :assetNo " +
            "AND (:status IS NULL OR a.status = :status) " +
            "AND (((:isScrollUp IS NULL OR :isScrollUp = false) AND a.interestDate <= :currentDate) " +
            "or (:isScrollUp = true AND a.interestDate > :currentDate)) " +
            "AND a.deleted = 0")
    Page<AssetInterestScheduleEntity> findByConditions(
            @Param("assetNo") String assetNo,
            @Param("status") Integer status,
            @Param("isScrollUp") Boolean isScrollUp,
            @Param("currentDate") LocalDate currentDate,
            Pageable pageable);

    @Query("SELECT sum(c.value) " +
            "FROM AssetInterestScheduleEntity c " +
            "WHERE c.assetNo = :assetNo " +
            "AND c.status = :status " +
            "AND c.deleted = 0")
    Float findSumByAssetNoAndStatus(@Param("assetNo") String assetNo, @Param("status") Integer status);

}


