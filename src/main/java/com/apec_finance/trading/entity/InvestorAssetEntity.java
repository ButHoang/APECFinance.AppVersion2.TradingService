package com.apec_finance.trading.entity;

import com.apec_finance.trading.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;

@Data
@Entity
@Table(name = "td_investor_asset")
public class InvestorAssetEntity extends BaseEntity {

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
    private Integer quantity = 0;

    @Column(name = "value", nullable = false)
    private Float value = 0f;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "origin_asset_no", length = 50)
    private String originAssetNo;

    @Column(name = "bo_certificate_no", length = 50)
    private String boCertificateNo;
}

