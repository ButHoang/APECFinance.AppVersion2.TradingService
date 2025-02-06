package com.apec_finance.trading.entity;

import com.apec_finance.trading.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "td_investor_asset_history")
public class InvestorAssetHistoryEntity extends BaseEntity {

    @Column(nullable = false)
    private OffsetDateTime tradingDate; // Ngày giao dịch

    @Column(nullable = false, length = 50)
    private String assetNo; // Số hiệu tài sản

    @Column(nullable = false)
    private Long investorId; // ID của nhà đầu tư (liên kết với bảng md_investor)

    @Column(nullable = false, length = 50)
    private String investorAccountNo; // Mã nhà đầu tư (liên kết với bảng md_investor)

    @Column(nullable = false)
    private int productId; // ID sản phẩm

    @Column(nullable = false, length = 50)
    private String productCode; // Mã sản phẩm

    @Column(nullable = false)
    private int quantity; // Số lượng sở hữu

    @Column(nullable = false)
    private float value; // Giá trị sở hữu

    @Column(nullable = false)
    private int status; // Trạng thái tài sản (0: Không kích hoạt, 1: Đang kích hoạt, 2: Bị khóa)

    @Column(length = 50)
    private String originAssetNo; // Số hiệu tài sản gốc (nếu có)
}
