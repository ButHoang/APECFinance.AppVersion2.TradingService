package com.apec_finance.trading.model.interest_schedule;

import com.apec_finance.trading.comon.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class InterestScheduleRQ extends BaseSearch {
    @NotNull
    private String assetNo;
    private Integer status;
    private Boolean isScrollUp;
}
