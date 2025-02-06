package com.apec_finance.trading.model.interest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Interest {
    private BigDecimal totalAssetValue;
    private BigDecimal value;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
