package com.apec_finance.trading.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateAsset {
    @NotBlank
    private Long total;
}
