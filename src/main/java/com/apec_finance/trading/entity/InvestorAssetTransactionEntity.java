package com.apec_finance.trading.entity;

import com.apec_finance.trading.comon.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "td_investor_asset_transaction")
@Accessors(chain = true)
public class InvestorAssetTransactionEntity extends BaseEntity {

    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "asset_no", length = 50)
    private String assetNo;

    @Column(name = "tran_no", nullable = false, length = 50)
    private String tranNo;

    @Column(name = "tran_date", nullable = false)
    private LocalDate tranDate;  // Ngày giao dịch, không lưu giờ

    @Column(name = "tran_time", nullable = false)
    private Time tranTime;  // Thời gian giao dịch, lưu cả giờ

    @Column(name = "tran_type", length = 50)
    private String tranType;  // Loại giao dịch (BUY, SELL, TRANSFER, MANTURITY, REWARD)

    @Column(name = "investor_id", nullable = false)
    private Long investorId;

    @Column(name = "investor_account_no", nullable = false, length = 50)
    private String investorAccountNo;

    @Column(name = "opr", nullable = false, length = 1)
    private String opr;  // Toán tử (+/-)

    @Column(name = "quantity", nullable = false)
    private Integer quantity;  // Số lượng

    @Column(name = "value", nullable = false)
    private Float value;  // Giá trị

    @Column(name = "status", nullable = false, length = 1)
    private String status;  // Trạng thái giao dịch (P, A, R, C)

    @Column(name = "af_quantity")
    private Float afQuantity;  // Số lượng sau giao dịch

    @Column(name = "af_value")
    private Float afValue;  // Giá trị sau giao dịch

    @Column(name = "ref_id")
    private Long refId;  // ID tham chiếu đến giao dịch gốc

    @Column(name = "ref_no", length = 255)
    private String refNo;  // Mã tham chiếu đến giao dịch gốc
}
