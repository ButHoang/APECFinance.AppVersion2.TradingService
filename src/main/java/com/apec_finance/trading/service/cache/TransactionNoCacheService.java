package com.apec_finance.trading.service.cache;

import com.apec_finance.trading.repository.InvestorAssetTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class TransactionNoCacheService {
    private static final String CACHE_NAME = "transactionNoCache";

    private final InvestorAssetTransactionRepository investorAssetTransactionRepository;

    @Cacheable(value = CACHE_NAME, key = "'tranNo_' + T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd')) + '_' + #investorId")
    public int getTransactionNoSequence() {
        LocalDate today = LocalDate.now();
        String tranNoPrefix = "T" + today.format(DateTimeFormatter.ofPattern("yyMMdd"));

        long transactionCount = investorAssetTransactionRepository.countInvestorAssetsTransByPrefix(tranNoPrefix);

        return (int) transactionCount;
    }

    @CacheEvict(value = CACHE_NAME, key = "'tranNo_' + T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd')) + '_' + #investorId")
    public int generateTransactionNo() {
        int currentSequence = getTransactionNoSequence();

        currentSequence++;

        return currentSequence;
    }

    public String generateTransactionNoForOrder() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        int transactionSequence = generateTransactionNo();

        return "T" + datePrefix + String.format("%04d", transactionSequence);
    }
}