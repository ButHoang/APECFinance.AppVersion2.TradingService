package com.apec_finance.trading.model.order;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class AppProduct {

    private Long id;

    @JsonProperty("status")
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

    private String code;

    private String name;

    @JsonProperty("stocks_type")
    private Integer stocksType;

    @JsonProperty("par_value")
    private Long parValue;

    @JsonProperty("total_qtty")
    private Long totalQtty;

    @JsonProperty("issue_date")
    private LocalDate issueDate;

    @JsonProperty("due_date")
    private LocalDate dueDate;

    @JsonProperty("stock_status")
    private String stockStatus;

    @JsonProperty("bond_period")
    private Integer bondPeriod;

    @JsonProperty("bond_period_unit")
    private Integer bondPeriodUnit;

    @JsonProperty("interest_period")
    private Integer interestPeriod;

    @JsonProperty("interest_rate")
    private Float interestRate;

    @JsonProperty("interest_period_unit")
    private String interestPeriodUnit;

    @JsonProperty("interest_type")
    private String interestType;

    @JsonProperty("interest_rate_type")
    private String interestRateType;

    @JsonProperty("interest_payment_type")
    private String interestPaymentType;

    private String description;

    @JsonProperty("base_int_days")
    private Integer baseIntDays;

    @JsonProperty("order_pending_day")
    private Integer orderPendingDay;

    @JsonProperty("repos_investor")
    private Integer reposInvestor;

    @JsonProperty("allow_buyback_date")
    private LocalDate allowBuybackDate;

    @JsonProperty("info_url")
    private String infoUrl;

    @JsonProperty("allow_online_trading")
    private Boolean allowOnlineTrading;

    @JsonProperty("has_product")
    private Boolean hasProduct;

    @JsonProperty("bank_acc_id")
    private Long bankAccId;

    private String category;

    @JsonProperty("auto_maturity")
    private Boolean autoMaturity;

    @JsonProperty("mobilizing")
    private Boolean mobilizing;

    @JsonProperty("issuer_id")
    private Long issuerId;

    private Integer type;

    @JsonProperty("stock_product_detail")
    private AppStockProductDetail stockProductDetail;
}

