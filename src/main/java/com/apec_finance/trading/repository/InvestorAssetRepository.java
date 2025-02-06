package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.InvestorAssetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestorAssetRepository extends JpaRepository<InvestorAssetEntity, Long> {
    @Query("SELECT ia FROM InvestorAssetEntity ia WHERE ia.investorId = :investorId " +
            "AND ia.status = :status " +
            "AND ia.deleted = 0 " +
            "AND ia.value > 0" +
            "AND ((:productIds) IS NULL OR ia.productId IN (:productIds))")
    Page<InvestorAssetEntity> findByInvestorIdAndStatusAndProductIds(Long investorId, @Param("status") Integer status, List<Integer> productIds, Pageable pageable);

    InvestorAssetEntity findByAssetNoAndStatusAndDeleted(String assetNo, Integer status, Integer deleted);
    List<InvestorAssetEntity> findByAssetNoIn(List<String> assetNos);

    @Query("SELECT distinct ia.investorAccountNo FROM InvestorAssetEntity ia WHERE ia.investorId = :investorId AND ia.status = :status AND ia.deleted = :deleted")
    String findInvestorAccountNoByInvestorIdAndStatusAndDeleted(@Param("investorId") Long investorId,
                                                                @Param("status") Integer status,
                                                                @Param("deleted") Integer deleted);
    List<InvestorAssetEntity> findByProductIdInAndStatusAndDeleted(List<Integer> productIds, Integer status, Integer deleted);

    @Query("SELECT ia.productId FROM InvestorAssetEntity ia " +
            "WHERE ia.status = 1 " +
            "AND ia.deleted = 0 ")
    List<Integer> getAllProductId();

    @Query("SELECT COUNT(a) FROM InvestorAssetEntity a WHERE a.assetNo LIKE :assetNoPrefix% ")
    long countInvestorAssetsByPrefixAndInvestorId(@Param("assetNoPrefix") String assetNoPrefix);

    @Query("SELECT SUM(ia.value) " +
            "FROM InvestorAssetEntity ia " +
            "WHERE ia.investorId = :investorId " +
            "AND ia.status = :status " +
            "AND ia.deleted = 0")
    Float getTotalValueByInvestorAndStatus(@Param("investorId") Long investorId, @Param("status") Integer status);
    @Query("SELECT SUM(ia.value) " +
            "FROM InvestorAssetEntity ia " +
            "WHERE ia.investorId = :investorId " +
            "AND ia.productId in (:productIds) " +
            "AND ia.status = 1 " +
            "AND ia.deleted = 0")
    Float getTotalValueByProductIdsAndInvestor(@Param("investorId") Long investorId, @Param("productIds") List<Integer> productIds);

    @Query("SELECT COUNT(ia) " +
            "FROM InvestorAssetEntity ia " +
            "WHERE ia.investorId = :investorId " +
            "AND ia.status = :status " +
            "AND ia.deleted = 0")
    Long countByInvestorAndStatus(@Param("investorId") Long investorId, @Param("status") Integer status);

}
