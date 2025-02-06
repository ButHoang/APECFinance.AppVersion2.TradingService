package com.apec_finance.trading.model.asset_transaction;

import com.apec_finance.trading.comon.BaseSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchAssetTransactionRQ extends BaseSearch {
    private String tranType;
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;
}
