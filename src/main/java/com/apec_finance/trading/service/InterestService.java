package com.apec_finance.trading.service;

import com.apec_finance.trading.model.interest.Interest;
import com.apec_finance.trading.model.interest.InterestSummary;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface InterestService {
    Interest getInterestInfo(Long investorId);
    InterestSummary getInterestSummary(List<Integer> productIds);
}
