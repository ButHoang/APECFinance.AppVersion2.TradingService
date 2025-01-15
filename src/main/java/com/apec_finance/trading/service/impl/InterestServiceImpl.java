package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.exception.validate.ValidationException;
import com.apec_finance.trading.model.interest.Interest;
import com.apec_finance.trading.model.interest.InterestSummary;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import com.apec_finance.trading.repository.InvestorAssetRepository;
import com.apec_finance.trading.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {
    private final AssetInterestScheduleRepository assetInterestScheduleRepository;
    private final InvestorAssetRepository investorAssetRepository;

    @Override
    public Interest getInterestInfo(Long investorId) {
        var interestSchedule = assetInterestScheduleRepository.findFirstByInvestorIdAndStatusWithEarliestInterestDate(investorId, 0);
        if (interestSchedule == null) throw new ValidationException("User Id not found");
        List<InvestorAssetEntity> investorAssetEntities = investorAssetRepository.findByInvestorIdAndProductIds(investorId, null, Pageable.unpaged()).getContent();

        float value = investorAssetEntities.stream()
                .map(InvestorAssetEntity::getValue)
                .reduce(0f, Float::sum);

        Float interestRate = interestSchedule.getInterestRate();
        Float feeAmount = interestSchedule.getFeeAmount();

        Interest rs = new Interest();
        BigDecimal assetFormat = BigDecimal.valueOf(value*interestRate - feeAmount);
        rs.setAsset(setAsset(assetFormat));
        rs.setDate(interestSchedule.getInterestDate());
        return rs;
    }

    @Override
    public InterestSummary getInterestSummary(List<Integer> productIds) {
        List<InvestorAssetEntity> investorAssetEntities = investorAssetRepository.findByProductIdInAndStatusAndDeleted(productIds, 1, 0);

        BigDecimal totalAsset = investorAssetEntities.stream()
                .map(entity -> BigDecimal.valueOf(entity.getValue()).setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<AssetInterestScheduleEntity> assetInterestSchedules = assetInterestScheduleRepository.findByProductIdIn(productIds);

        BigDecimal totalReceivedInterestValue = assetInterestSchedules.stream()
                .filter(entity -> entity.getStatus() == 1)
                .map(entity -> BigDecimal.valueOf(entity.getInterestValue())
                        .subtract(BigDecimal.valueOf(entity.getFeeAmount()))
                        .setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpectedInterestValue = assetInterestSchedules.stream()
                .map(entity -> BigDecimal.valueOf(entity.getInterestValue())
                        .subtract(BigDecimal.valueOf(entity.getFeeAmount()))
                        .setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        InterestSummary interestSummary = new InterestSummary();
        interestSummary.setTotalAsset(totalAsset);
        interestSummary.setNumOfAsset(investorAssetEntities.size());
        interestSummary.setTotalReceivedInterestValue(totalReceivedInterestValue);
        interestSummary.setTotalExpectedInterestValue(totalExpectedInterestValue);

        return interestSummary;
    }



    public BigDecimal setAsset(BigDecimal totalBalance) {
        return totalBalance.setScale(2, RoundingMode.HALF_UP);
    }
}

