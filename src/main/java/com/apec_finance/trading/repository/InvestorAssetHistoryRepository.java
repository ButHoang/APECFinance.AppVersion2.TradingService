package com.apec_finance.trading.repository;

import com.apec_finance.trading.entity.InvestorAssetHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorAssetHistoryRepository extends JpaRepository<InvestorAssetHistoryEntity, Long> {
}
