package com.apec_finance.trading.model.asset;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class Asset {
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate;
    private String investorAccountNo;
    private Integer productId;
    private String productCode;
    private BigDecimal value;
    private String assetNo;
    private BigDecimal availableWithdrawBalance;
    private BigDecimal receivedInterestValue;
    private BigDecimal unReceivedInterestValue;
    private BigDecimal expectedInterestValue;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate accrualWithdrawalDate;
}
