package com.apec_finance.trading.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class AppStockProductDetail {

    private Long id;

    private String status;

    private Integer sort;

    @JsonProperty("user_created")
    private String userCreated;

    @JsonProperty("date_created")
    private OffsetDateTime dateCreated;

    @JsonProperty("user_updated")
    private String userUpdated;

    @JsonProperty("date_updated")
    private OffsetDateTime dateUpdated;

    @JsonProperty("stock_id")
    private Long stockId;

    private String code;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_period_unit")
    private String productPeriodUnit;

    @JsonProperty("interest_rate")
    private Integer interestRate;

    @JsonProperty("product_period")
    private Integer productPeriod;

    @JsonProperty("interest_period")
    private Integer interestPeriod;

    @JsonProperty("interest_period_unit")
    private String interestPeriodUnit;

    @JsonProperty("interest_payment_type")
    private String interestPaymentType;

    @JsonProperty("order_pending_day")
    private Integer orderPendingDay;

    @JsonProperty("allow_online_trading")
    private Boolean allowOnlineTrading;

    @JsonProperty("active_status")
    private String activeStatus;

    private String description;

    @JsonProperty("online_sell_type")
    private String onlineSellType;

    @JsonProperty("limit_value")
    private Long limitValue;

    @JsonProperty("due_date")
    private LocalDate dueDate;

    @JsonProperty("int_received_type")
    private String intReceivedType;

    @JsonProperty("ext_period")
    private Integer extPeriod;

    @JsonProperty("ext_period_unit")
    private String extPeriodUnit;

    @JsonProperty("ext_date")
    private LocalDate extDate;

    @JsonProperty("set_limit_date")
    private LocalDate setLimitDate;
}

