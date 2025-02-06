package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.mapper.AssetInterestScheduleMapper;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleDetail;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRQ;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRS;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import com.apec_finance.trading.service.InterestScheduleService;
import com.nimbusds.jose.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestScheduleServiceImpl implements InterestScheduleService {

    private final AssetInterestScheduleRepository assetInterestScheduleRepository;
    private final AssetInterestScheduleMapper interestScheduleMapper;

    @Override
    public InterestScheduleRS getInterestSchedule(InterestScheduleRQ rq) {
        LocalDate currentDate = LocalDate.now();

        Pageable pageable = (rq.getSize() > 0)
                ? PageRequest.of(rq.getPage(), rq.getSize(), Sort.by(Sort.Order.asc("interestDate")))
                : Pageable.unpaged();

        Boolean isScrollUp = rq.getIsScrollUp();

        Page<AssetInterestScheduleEntity> resultPage = assetInterestScheduleRepository.findByConditions(
                rq.getAssetNo(),
                rq.getStatus(),
                isScrollUp,
                currentDate,
                pageable
        );

        List<InterestScheduleDetail> interestScheduleDetails = interestScheduleMapper.getDetails(resultPage.getContent());

        InterestScheduleRS interestScheduleRS = new InterestScheduleRS();

        Tuple totalInterestNotReceivedTuple = assetInterestScheduleRepository.findSumAndCountByAssetNoAndStatus(rq.getAssetNo(), 0);
        Tuple totalInterestReceivedTuple = assetInterestScheduleRepository.findSumAndCountByAssetNoAndStatus(rq.getAssetNo(), 1);

        double totalInterestNotReceivedDouble = (totalInterestNotReceivedTuple.get("totalInterest") != null) ? (Double) totalInterestNotReceivedTuple.get("totalInterest") : 0d;
        Integer totalInterestNotReceivedCount = (totalInterestNotReceivedTuple.get("totalCount") != null) ? ((Long) totalInterestNotReceivedTuple.get("totalCount")).intValue() : 0;

        double totalInterestReceivedDouble = (totalInterestReceivedTuple.get("totalInterest") != null) ? (Double) totalInterestReceivedTuple.get("totalInterest") : 0d;
        Integer totalInterestReceivedCount = (totalInterestReceivedTuple.get("totalCount") != null) ? ((Long) totalInterestReceivedTuple.get("totalCount")).intValue() : 0;

        BigDecimal totalInterestNotReceived = BigDecimal.valueOf(totalInterestNotReceivedDouble)
                .setScale(1, RoundingMode.HALF_UP);

        BigDecimal totalInterestReceived = BigDecimal.valueOf(totalInterestReceivedDouble)
                .setScale(1, RoundingMode.HALF_UP);

        interestScheduleRS.setTotalInterestReceived(totalInterestReceived);
        interestScheduleRS.setTotalInterestNotReceived(totalInterestNotReceived);
        interestScheduleRS.setNumOfInterestNotReceived(totalInterestNotReceivedCount);
        interestScheduleRS.setNumOfInterestReceived(totalInterestReceivedCount);

        PaginationRS<InterestScheduleDetail> paginationRS = new PaginationRS<>();
        paginationRS.setContent(interestScheduleDetails);
        paginationRS.setPageNumber(resultPage.getNumber());
        paginationRS.setPageSize(resultPage.getSize());
        paginationRS.setTotalElements(resultPage.getTotalElements());
        paginationRS.setTotalPages(resultPage.getTotalPages());

        interestScheduleRS.setInterestScheduleDetails(paginationRS);

        return interestScheduleRS;
    }



}


