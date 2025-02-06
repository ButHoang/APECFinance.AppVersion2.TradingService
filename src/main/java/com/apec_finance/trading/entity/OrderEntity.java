package com.apec_finance.trading.entity;

import com.apec_finance.trading.comon.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "td_order")
@Accessors(chain = true)
public class OrderEntity extends BaseEntity {

    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;  // Số hiệu lệnh

    @Column(name = "investor_id", nullable = false)
    private Long investorId;  // ID nhà đầu tư, liên kết với bảng md_investor

    @Column(name = "investor_account_no", nullable = false, length = 50)
    private String investorAccountNo;  // Mã của nhà đầu tư

    @Column(name = "co_investor_id", nullable = true)
    private Long coInvestorId;  // ID nhà đầu tư đối ứng (nullable)

    @Column(name = "co_investor_account_no", length = 50)
    private String coInvestorAccountNo;  // Mã nhà đầu tư đối ứng (nullable)

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;  // Ngày đặt

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;  // Thời gian đặt

    @Column(name = "order_side", nullable = false, length = 1)
    private String orderSide;  // Mua/Bán

    @Column(name = "order_type", nullable = false)
    private Integer orderType;  // Loại lệnh

    @Column(name = "order_status", nullable = false)
    private Integer orderStatus;  // Trạng thái lệnh

    @Column(name = "product_id", nullable = false)
    private Integer productId;  // ID sản phẩm

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;  // Mã sản phẩm

    @Column(name = "asset_no", length = 50)
    private String assetNo;  // Mã tài sản

    @Column(name = "par_value", nullable = false)
    private Float parValue;  // Mệnh giá của sản phẩm

    @Column(name = "order_quantity", nullable = false)
    private Integer orderQuantity;  // Số lượng đặt

    @Column(name = "order_price", nullable = false)
    private Float orderPrice;  // Giá đặt

    @Column(name = "order_value", nullable = false)
    private Float orderValue;  // Giá trị đặt

    @Column(name = "interest_rate", nullable = true)
    private Float interestRate;  // Lãi suất (nullable)

    @Column(name = "interest_value", nullable = true)
    private Float interestValue;  // Giá trị lãi nhận được (nullable)

    @Column(name = "fee_rate", nullable = true)
    private Float feeRate;  // Tỉ lệ phí/thuế (nullable)

    @Column(name = "fee_amount", nullable = true)
    private Float feeAmount;  // Số tiền phí/thuế (nullable)

    @Column(name = "paid_amount", nullable = false)
    private Float paidAmount;  // Số tiền thanh toán cho lệnh

    @Column(name = "remark", length = 512)
    private String remark;  // Ghi chú (nullable)

    @Column(name = "verified_date", nullable = false)
    private OffsetDateTime verifiedDate;  // Ngày xác nhận

    @Column(name = "verified_by", length = 255)
    private String verifiedBy;  // Người xác nhận
}

