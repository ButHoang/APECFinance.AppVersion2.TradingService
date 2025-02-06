package com.apec_finance.trading.service.cache;

import com.apec_finance.trading.repository.InvestorAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AssetNoCacheService {
    private static final String CACHE_NAME = "assetNoCache";

    private final InvestorAssetRepository investorAssetRepository;

    @Cacheable(value = CACHE_NAME, key = "'assetNo_' + T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd')) + '_' + #investorId")
    public int getAssetNoSequence() {
        LocalDate today = LocalDate.now();
        String assetNoPrefix = "A" + today.format(DateTimeFormatter.ofPattern("yyMMdd"));

        long assetCount = investorAssetRepository.countInvestorAssetsByPrefixAndInvestorId(assetNoPrefix);

        return (int) assetCount;
    }

    @CacheEvict(value = CACHE_NAME, key = "'assetNo_' + T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd')) + '_' + #investorId")
    public int generateAssetNo() {
        int currentSequence = getAssetNoSequence();

        currentSequence++;

        return currentSequence;
    }
    public String generateAssetNoForOrder() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        int assetSequence = generateAssetNo();

        return "A" + datePrefix + String.format("%04d", assetSequence);
    }
}
