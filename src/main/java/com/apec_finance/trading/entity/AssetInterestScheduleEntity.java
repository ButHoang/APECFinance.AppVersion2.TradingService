package com.apec_finance.trading.entity;

import com.apec_finance.trading.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "td_asset_int_schedule")
public class AssetInterestScheduleEntity extends BaseEntity {

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(name = "asset_no", nullable = false, length = 50)
    private String assetNo;

    @Column(name = "investor_id", nullable = false)
    private Long investorId;

    @Column(name = "investor_account_no", nullable = false, length = 50)
    private String investorAccountNo;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "value", nullable = false)
    private Float value;

    @Column(name = "interest_date", nullable = false)
    private LocalDate interestDate;

    @Column(name = "interest_rate", nullable = false)
    private Float interestRate;

    @Column(name = "interest_value", nullable = false)
    private Float interestValue;

    @Column(name = "fee_rate", nullable = false)
    private Float feeRate;

    @Column(name = "fee_amount", nullable = false)
    private Float feeAmount;

    @Column(name = "description", nullable = true, length = 512)
    private String description;

    @Column(name = "status", nullable = false)
    private Integer status;

}
