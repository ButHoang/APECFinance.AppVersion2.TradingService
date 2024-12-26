package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.InvestorAssetTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorAssetTransactionRepository extends JpaRepository<InvestorAssetTransactionEntity, Long> {
}
