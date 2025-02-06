package com.apec_finance.trading.model.asset_transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AssetTransaction {
    private String tranType;
    private String opr;
    private BigDecimal value;
    private String assetNo;
    private String refNo;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdDate;
    private String status;
    private Integer productId;
    private BigDecimal afValue;
}
