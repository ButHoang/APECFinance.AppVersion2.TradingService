package com.apec_finance.trading.model.order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class AppProduct {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("sort")
    private String sort;

    @JsonProperty("user_created")
    private String userCreated;

    @JsonProperty("date_created")
    private OffsetDateTime dateCreated;

    @JsonProperty("user_updated")
    private String userUpdated;

    @JsonProperty("date_updated")
    private OffsetDateTime dateUpdated;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("product_type")
    private String productType;

    @JsonProperty("issue_date")
    private LocalDate issueDate;

    @JsonProperty("due_date")
    private LocalDate dueDate;

    @JsonProperty("product_status")
    private String productStatus;

    @JsonProperty("period")
    private Integer period;

    @JsonProperty("period_unit")
    private Integer periodUnit;

    @JsonProperty("interest_period")
    private Integer interestPeriod;

    @JsonProperty("interest_rate")
    private Float interestRate;

    @JsonProperty("interest_period_unit")
    private Integer interestPeriodUnit;

    @JsonProperty("interest_type")
    private Integer interestType;

    @JsonProperty("interest_rate_type")
    private Integer interestRateType;

    @JsonProperty("interest_payment_type")
    private String interestPaymentType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("order_pending_day")
    private Integer orderPendingDay;

    @JsonProperty("repos_investor")
    private Integer reposInvestor;

    @JsonProperty("allow_buyback_date")
    private LocalDate allowBuybackDate;

    @JsonProperty("info_url")
    private String infoUrl;

    @JsonProperty("has_product")
    private String hasProduct;

    @JsonProperty("bank_acc_id")
    private Integer bankAccId;

    @JsonProperty("category")
    private String category;

    @JsonProperty("auto_maturity")
    private String autoMaturity;

    @JsonProperty("mobilizing")
    private String mobilizing;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("online_sell_type")
    private String onlineSellType;

    @JsonProperty("int_received_type")
    private String intReceivedType;

    @JsonProperty("ext_period")
    private Integer extPeriod;

    @JsonProperty("ext_period_unit")
    private Integer extPeriodUnit;

    @JsonProperty("ext_date")
    private LocalDate extDate;

    @JsonProperty("set_limit_date")
    private LocalDate setLimitDate;

    @JsonProperty("is_show_on_app")
    private String isShowOnApp;

    @JsonProperty("par_value")
    private Integer parValue;

    @JsonProperty("base_int_days")
    private Integer baseIntDays;

    @JsonProperty("total_qtty")
    private Integer totalQtty;

    @JsonProperty("limit_value")
    private Integer limitValue;

    @JsonProperty("product_int_detail")
    private List<AppStockProductDetail> productIntDetail;

    @JsonProperty("issuer_id")
    private AppIssuer issuerId;
}


