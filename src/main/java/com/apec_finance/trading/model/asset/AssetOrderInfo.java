package com.apec_finance.trading.model.asset;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AssetOrderInfo {
    private String assetNo;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate verifyDate;
    private BigDecimal orderValue;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate maturityDate;
}
