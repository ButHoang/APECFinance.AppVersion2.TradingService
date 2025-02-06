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
        var interestSchedules = assetInterestScheduleRepository.findByInvestorIdAndStatusWithEarliestInterestDateAfterNow(investorId, 0);
        if (interestSchedules.isEmpty()) throw new ValidationException("There is no interest schedule by investorId:" + investorId);
        List<InvestorAssetEntity> investorAssetEntities = investorAssetRepository.findByInvestorIdAndStatusAndProductIds(investorId, 1,null, Pageable.unpaged()).getContent();

        float value = investorAssetEntities.stream()
                .map(InvestorAssetEntity::getValue)
                .reduce(0f, Float::sum);


        Interest rs = new Interest();
        rs.setTotalAssetValue(formatNumber(BigDecimal.valueOf(value)));
        rs.setValue(calculateTotalValue(interestSchedules));
        rs.setDate(interestSchedules.get(0).getInterestDate());

        return rs;
    }

    public BigDecimal calculateTotalValue(List<AssetInterestScheduleEntity> schedules) {
        BigDecimal totalInterestValue = schedules.stream()
                .map(AssetInterestScheduleEntity::getInterestValue)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFeeAmount = schedules.stream()
                .map(AssetInterestScheduleEntity::getFeeAmount)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal result = totalInterestValue.subtract(totalFeeAmount);
        return formatNumber(result);
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


    public BigDecimal formatNumber(BigDecimal number) {
        return number.setScale(1, RoundingMode.HALF_UP);
    }
}

