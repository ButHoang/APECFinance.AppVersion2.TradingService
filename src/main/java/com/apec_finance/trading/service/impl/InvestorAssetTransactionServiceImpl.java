package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.entity.InvestorAssetTransactionEntity;
import com.apec_finance.trading.model.asset_transaction.AssetTransaction;
import com.apec_finance.trading.model.asset_transaction.SearchAssetTransactionRQ;
import com.apec_finance.trading.repository.InvestorAssetRepository;
import com.apec_finance.trading.repository.InvestorAssetTransactionRepository;
import com.apec_finance.trading.service.InvestorAssetTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestorAssetTransactionServiceImpl implements InvestorAssetTransactionService {
    private final InvestorAssetTransactionRepository investorAssetTransactionRepository;
    private final InvestorAssetRepository investorAssetRepository;
    @Override
    public PaginationRS<AssetTransaction> getListAssetTransaction(SearchAssetTransactionRQ rq) {
        Pageable pageable;

        if (rq.getPage() != 0 && rq.getSize() != 0) {
            pageable = PageRequest.of(rq.getPage(), rq.getSize(), Sort.by(Sort.Order.desc("id")));
        } else {
            pageable = Pageable.unpaged();
        }

        Page<InvestorAssetTransactionEntity> pageResult = investorAssetTransactionRepository.searchTransactions(
                rq.getFromDate(),
                rq.getToDate(),
                rq.getTranType(),
                rq.getStatus(),
                pageable
        );

        List<String> assetNos = pageResult.getContent().stream()
                .map(InvestorAssetTransactionEntity::getAssetNo)
                .distinct()
                .collect(Collectors.toList());

        Map<String, InvestorAssetEntity> assetMap = new HashMap<>();
        if (!assetNos.isEmpty()) {
            List<InvestorAssetEntity> assetEntities = investorAssetRepository.findByAssetNoIn(assetNos);
            assetEntities.forEach(assetEntity -> assetMap.put(assetEntity.getAssetNo(), assetEntity));
        }

        List<AssetTransaction> assetTransactions = pageResult.getContent().stream()
                .map(transaction -> {
                    AssetTransaction assetTransaction = new AssetTransaction();
                    assetTransaction.setTranType(transaction.getTranType());
                    assetTransaction.setOpr(transaction.getOpr());
                    assetTransaction.setValue(BigDecimal.valueOf(transaction.getValue()).setScale(1, RoundingMode.HALF_UP));
                    assetTransaction.setAssetNo(transaction.getAssetNo());
                    assetTransaction.setRefNo(transaction.getRefNo());
                    assetTransaction.setCreatedDate(transaction.getCreatedDate());
                    assetTransaction.setStatus(transaction.getStatus());
                    assetTransaction.setAfValue(BigDecimal.valueOf(transaction.getAfValue()).setScale(1, RoundingMode.HALF_UP));

                    InvestorAssetEntity assetEntity = assetMap.get(transaction.getAssetNo());
                    if (assetEntity != null) {
                        assetTransaction.setProductId(assetEntity.getProductId());
                    }

                    return assetTransaction;
                })
                .collect(Collectors.toList());

        PaginationRS<AssetTransaction> paginationRS = new PaginationRS<>();
        paginationRS.setContent(assetTransactions);
        paginationRS.setPageNumber(pageResult.getNumber());
        paginationRS.setPageSize(pageResult.getSize());
        paginationRS.setTotalElements(pageResult.getTotalElements());
        paginationRS.setTotalPages(pageResult.getTotalPages());

        return paginationRS;
    }
}
