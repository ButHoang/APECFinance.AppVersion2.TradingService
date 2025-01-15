package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.nimbusds.jose.util.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.List;

public interface AssetInterestScheduleRepository extends JpaRepository<AssetInterestScheduleEntity, Long> {
    List<AssetInterestScheduleEntity> findByProductIdIn(List<Integer> productIds);
    List<AssetInterestScheduleEntity> findByAssetIdAndInterestDateGreaterThan(Long assetId, LocalDate currentDate);

    @Query(value = "SELECT c.* FROM td_asset_int_schedule c " +
            "WHERE c.investor_id = :investorId " +
            "AND c.status = :status " +
            "AND c.deleted = 0 " +
            "AND c.interest_date >= CURRENT_DATE " +
            "ORDER BY c.interest_date ASC " +
            "LIMIT 1", nativeQuery = true)
    AssetInterestScheduleEntity findFirstByInvestorIdAndStatusWithEarliestInterestDate(
            @Param("investorId") Long investorId,
            @Param("status") Integer status);


    List<AssetInterestScheduleEntity> findByAssetNoAndStatusAndDeleted(String assetNo, Integer status, Integer deleted);

    @Query("SELECT a FROM AssetInterestScheduleEntity a " +
            "WHERE a.assetNo = :assetNo " +
            "AND (:status IS NULL OR a.status = :status) " +
            "AND (((:isScrollUp IS NULL OR :isScrollUp = false) AND a.interestDate <= :currentDate) " +
            "or (:isScrollUp = true AND a.interestDate > :currentDate)) " +
            "AND a.deleted = 0 " +
            "ORDER BY a.interestDate DESC")
    Page<AssetInterestScheduleEntity> findByConditions(
            @Param("assetNo") String assetNo,
            @Param("status") Integer status,
            @Param("isScrollUp") Boolean isScrollUp,
            @Param("currentDate") LocalDate currentDate,
            Pageable pageable);

    @Query("SELECT SUM(c.value) AS totalInterest, COUNT(c) AS totalCount " +
            "FROM AssetInterestScheduleEntity c " +
            "WHERE c.assetNo = :assetNo " +
            "AND c.status = :status " +
            "AND c.deleted = 0")
    Tuple findSumAndCountByAssetNoAndStatus(@Param("assetNo") String assetNo, @Param("status") Integer status);

}


