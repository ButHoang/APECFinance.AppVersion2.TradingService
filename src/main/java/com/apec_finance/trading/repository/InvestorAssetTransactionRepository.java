package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.InvestorAssetTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface InvestorAssetTransactionRepository extends JpaRepository<InvestorAssetTransactionEntity, Long> {
    @Query("SELECT COUNT(iat) FROM InvestorAssetTransactionEntity iat WHERE iat.tranNo LIKE :tranNoPrefix% ")
    long countInvestorAssetsTransByPrefix(@Param("tranNoPrefix") String tranNoPrefix);
    InvestorAssetTransactionEntity findByRefId(Long refId);

    @Query("SELECT iat FROM InvestorAssetTransactionEntity iat " +
            "WHERE (DATE(:fromDate) IS NULL OR iat.tranDate >= DATE(:fromDate)) " +
            "AND (DATE(:toDate) IS NULL OR iat.tranDate <= DATE(:toDate)) " +
            "AND (:orderType IS NULL OR iat.tranType = :orderType) " +
            "AND (:status IS NULL OR iat.status = :status) " +
            "ORDER BY iat.id DESC")
    Page<InvestorAssetTransactionEntity> searchTransactions(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("orderType") String orderType,
            @Param("status") String status,
            Pageable pageable);

}
