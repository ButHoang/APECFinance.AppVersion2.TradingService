package com.apec_finance.trading.model.interest_schedule;

import com.apec_finance.trading.comon.PaginationRS;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InterestScheduleRS {
    private BigDecimal totalInterestNotReceived;
    private BigDecimal totalInterestReceived;
    private Integer numOfInterestNotReceived;
    private Integer numOfInterestReceived;
    private PaginationRS<InterestScheduleDetail> interestScheduleDetails;
}
