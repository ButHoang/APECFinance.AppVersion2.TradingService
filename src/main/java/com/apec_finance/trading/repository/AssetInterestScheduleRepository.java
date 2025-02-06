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
            "AND c.interest_date = (SELECT MIN(c2.interest_date) " +
            "FROM td_asset_int_schedule c2 " +
            "WHERE c2.investor_id = :investorId " +
            "AND c2.status = :status " +
            "AND c2.deleted = 0 " +
            "AND c2.interest_date >= CURRENT_DATE) " +
            "ORDER BY c.interest_date ASC", nativeQuery = true)
    List<AssetInterestScheduleEntity> findByInvestorIdAndStatusWithEarliestInterestDateAfterNow(
            @Param("investorId") Long investorId,
            @Param("status") Integer status);



    List<AssetInterestScheduleEntity> findByAssetNoAndStatusAndDeleted(String assetNo, Integer status, Integer deleted);

    @Query("SELECT a FROM AssetInterestScheduleEntity a " +
            "WHERE a.assetNo = :assetNo " +
            "AND (:status IS NULL OR a.status = :status) " +
            "AND (((:isScrollUp IS NULL OR :isScrollUp = false) AND a.interestDate <= :currentDate) " +
            "or (:isScrollUp = true AND a.interestDate > :currentDate)) " +
            "AND a.deleted = 0 ")
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

    @Query("SELECT a.interestDate FROM AssetInterestScheduleEntity a " +
            "WHERE a.assetNo = :assetNo " +
            "AND a.deleted = 0 " +
            "AND a.status = 0 " +
            "AND a.interestDate = (SELECT MAX(b.interestDate) FROM AssetInterestScheduleEntity b " +
            "WHERE b.assetNo = :assetNo AND b.deleted = 0 AND b.status = 0)")
    LocalDate findLatestInterestDateByAssetNo(@Param("assetNo") String assetNo);


}


