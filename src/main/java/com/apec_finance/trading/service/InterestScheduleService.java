package com.apec_finance.trading.service;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRQ;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRS;

public interface InterestScheduleService {
    InterestScheduleRS getInterestSchedule(InterestScheduleRQ rq);
}
