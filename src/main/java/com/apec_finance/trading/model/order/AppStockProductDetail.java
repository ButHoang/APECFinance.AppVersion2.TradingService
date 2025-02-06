package com.apec_finance.trading.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
public class AppStockProductDetail {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("sort")
    private Integer sort;

    @JsonProperty("user_created")
    private String userCreated;

    @JsonProperty("date_created")
    private OffsetDateTime dateCreated;

    @JsonProperty("user_updated")
    private String userUpdated;

    @JsonProperty("date_updated")
    private OffsetDateTime dateUpdated;

    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("interest_period")
    private Integer interestPeriod;

    @JsonProperty("interest_period_unit")
    private Integer interestPeriodUnit;

    @JsonProperty("Deleted")
    private String deleted;

    @JsonProperty("interest_rate")
    private Integer interestRate;
}




