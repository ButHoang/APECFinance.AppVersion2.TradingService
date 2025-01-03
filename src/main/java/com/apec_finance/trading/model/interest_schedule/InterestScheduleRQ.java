package com.apec_finance.trading.model.interest_schedule;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InterestScheduleRQ {
    @NotNull
    private String assetNo;
    private int page;
    private int size;
    private Integer status;
    private Boolean isScrollUp;
}
