package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.entity.AssetInterestScheduleEntity;
import com.apec_finance.trading.mapper.AssetInterestScheduleMapper;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleDetail;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRQ;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRS;
import com.apec_finance.trading.repository.AssetInterestScheduleRepository;
import com.apec_finance.trading.service.InterestScheduleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
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
                ? PageRequest.of(rq.getPage(), rq.getSize())
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

        Float totalInterestNotReceivedFloat = assetInterestScheduleRepository.findSumByAssetNoAndStatus(rq.getAssetNo(), 0);
        Float totalInterestReceivedFloat = assetInterestScheduleRepository.findSumByAssetNoAndStatus(rq.getAssetNo(), 1);

        BigDecimal totalInterestNotReceived = (totalInterestNotReceivedFloat != null)
                ? BigDecimal.valueOf(totalInterestNotReceivedFloat)
                : BigDecimal.ZERO;

        BigDecimal totalInterestReceived = (totalInterestReceivedFloat != null)
                ? BigDecimal.valueOf(totalInterestReceivedFloat)
                : BigDecimal.ZERO;

        interestScheduleRS.setTotalInterestReceived(totalInterestReceived);
        interestScheduleRS.setTotalInterestNotReceived(totalInterestNotReceived);

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


