package com.apec_finance.trading.model.asset;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;


@Data
public class Asset {
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate;
    private String investorAccountNo;
    private Integer productId;
    private String productCode;
    private Float value = 0f;
    private String assetNo;
}
