package com.apec_finance.trading.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateCashTransaction {
    private LocalDate tranDate;
    private LocalDateTime tranTime;
    private String tranType;
    private String opr;
    private Float tranAmount;
    private Long refId;
    private String refNo;
    private String status;
    private Long investorId;
    private String createdBy;
}
