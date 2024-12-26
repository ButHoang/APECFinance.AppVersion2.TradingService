package com.apec_finance.trading.entity;

import com.apec_finance.trading.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "td_investor_asset_transaction")
public class InvestorAssetTransactionEntity extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String tranNo; // Mã giao dịch

    @Column(nullable = false)
    private OffsetDateTime tranDate; // Ngày giao dịch (không lưu giờ)

    @Column(nullable = false)
    private OffsetDateTime tranTime; // Thời gian giao dịch (lưu cả giờ)

    @Column(length = 50)
    private String tranType; // Loại giao dịch (BUY, SELL, TRANSFER, MANTURITY, REWARD)

    @Column(nullable = false)
    private Long investorId; // ID nhà đầu tư

    @Column(nullable = false, length = 50)
    private String investorAccountNo; // Số tài khoản nhà đầu tư

    @Column(nullable = false, length = 1)
    private String opr; // Toán tử (+/-), gồm các giá trị: +, -

    @Column(nullable = false)
    private int quantity; // Số lượng giao dịch

    @Column(nullable = false)
    private float value; // Giá trị giao dịch

    @Column(nullable = false, length = 1)
    private String status; // Trạng thái giao dịch (P, A, R, C)

    @Column
    private float afQuantity; // Số lượng sau giao dịch (nếu có)

    @Column
    private float afValue; // Giá trị sau giao dịch (nếu có)
}
