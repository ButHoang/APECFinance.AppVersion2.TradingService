package com.apec_finance.trading.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRange {

    @JsonProperty("grname")
    private String grName;

    @JsonProperty("varname")
    private String varName;

    @JsonProperty("varvalue")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime varValue;
}
