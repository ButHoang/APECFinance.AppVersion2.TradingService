package com.apec_finance.trading.service;

import com.apec_finance.trading.model.Interest;

public interface InterestService {
    Interest getInterestInfo(Long investorId);
}
