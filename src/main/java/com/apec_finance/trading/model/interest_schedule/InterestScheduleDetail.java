package com.apec_finance.trading.model.interest_schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InterestScheduleDetail {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate interestDate;
    private BigDecimal interestRate;
    private BigDecimal interestValue;
    private Integer status;
}
