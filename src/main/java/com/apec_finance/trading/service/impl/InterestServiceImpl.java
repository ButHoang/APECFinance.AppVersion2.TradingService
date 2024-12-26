package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.entity.InvestorAssetEntity;
import com.apec_finance.trading.exception.validate.ValidationException;
import com.apec_finance.trading.model.interest.Interest;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import com.apec_finance.trading.repository.InvestorAssetRepository;
import com.apec_finance.trading.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {
    private final AssetInterestScheduleRepository assetInterestScheduleRepository;
    private final InvestorAssetRepository investorAssetRepository;

    @Override
    public Interest getInterestInfo(Long investorId) {
        var interestSchedule = assetInterestScheduleRepository.findByInvestorIdAndStatus(investorId, 0);
        if (interestSchedule == null) throw new ValidationException("User Id not found");
        InvestorAssetEntity investorAssetEntity = investorAssetRepository.findByInvestorIdAndStatusAndDeleted(investorId, 1, 0);
        Float value = investorAssetEntity.getValue();
        Float interestRate = interestSchedule.getInterestRate();
        Float feeAmount = interestSchedule.getFeeAmount();

        Interest rs = new Interest();
        BigDecimal assetFormat = BigDecimal.valueOf(value*interestRate - feeAmount);
        rs.setAsset(setAsset(assetFormat));
        rs.setDate(interestSchedule.getInterestDate());
        return rs;
    }

    public BigDecimal setAsset(BigDecimal totalBalance) {
        return totalBalance.setScale(2, RoundingMode.HALF_UP);
    }
}

