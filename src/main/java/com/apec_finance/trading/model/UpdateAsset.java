package com.apec_finance.trading.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAsset {
    @NotNull
    private Long id;
}
