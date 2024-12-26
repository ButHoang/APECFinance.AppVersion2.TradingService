package com.apec_finance.trading.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class Order {
    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    private Long investorId;

    private String investorAccountNo;

    private Long coInvestorId;  // ID nhà đầu tư đối ứng (nullable)

    private String coInvestorAccountNo;  // Mã nhà đầu tư đối ứng (nullable)

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate orderDate;  // Ngày đặt

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime orderTime;  // Thời gian đặt

    private String orderSide;  // Mua/Bán

    private Integer orderType;  // Loại lệnh

    private Integer orderStatus;  // Trạng thái lệnh

    private Integer productId;  // ID sản phẩm

    private String productCode;  // Mã sản phẩm

    private String assetNo;  // Mã tài sản

    private Float parValue;  // Mệnh giá của sản phẩm

    private Integer orderQuantity;  // Số lượng đặt

    private Float orderPrice;  // Giá đặt

    private Float orderValue;  // Giá trị đặt

    private Float interestRate;  // Lãi suất (nullable)

    private Float interestValue;  // Giá trị lãi nhận được (nullable)

    private Float feeRate;  // Tỉ lệ phí/thuế (nullable)

    private Float feeAmount;  // Số tiền phí/thuế (nullable)

    private Float paidAmount;  // Số tiền thanh toán cho lệnh

    private String remark;  // Ghi chú (nullable)
}
