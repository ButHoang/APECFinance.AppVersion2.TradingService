package com.apec_finance.trading.service;

import com.apec_finance.trading.model.interest.Interest;

public interface InterestService {
    Interest getInterestInfo(Long investorId);
}
