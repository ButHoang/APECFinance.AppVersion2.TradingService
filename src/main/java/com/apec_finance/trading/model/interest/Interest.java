package com.apec_finance.trading.model.interest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Interest {
    BigDecimal asset;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate date;
}
