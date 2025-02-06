package com.apec_finance.trading.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderDepositRQ {
    private Integer productId;
    private String productCode;
    private BigDecimal amount;
    private Long sumInvestmentLimit;
    private Long personalInvestmentLimit;
    private String investorAccountNo;
    private Integer orderQuantity;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
