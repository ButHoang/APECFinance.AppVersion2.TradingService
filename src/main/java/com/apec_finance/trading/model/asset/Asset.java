package com.apec_finance.trading.model.asset;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class Asset {
    private Long id;
    private LocalDateTime createdDate;
    private String investorAccountNo;
    private Integer productId;
    private String productCode;
    private Float value = 0f;
}
