package com.apec_finance.trading.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class AppIssuer {

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

    @JsonProperty("name_en")
    private String nameEn;

    @JsonProperty("short_name")
    private String shortName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("fax")
    private String fax;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("email")
    private String email;

    @JsonProperty("tax_code")
    private String taxCode;

    @JsonProperty("bank_acc_no")
    private String bankAccNo;

    @JsonProperty("bank_id")
    private Integer bankId;

    @JsonProperty("bank_branchname")
    private String bankBranchName;

    @JsonProperty("license_no")
    private String licenseNo;

    @JsonProperty("license_date")
    private LocalDate licenseDate;

    @JsonProperty("license_issuer")
    private String licenseIssuer;

    @JsonProperty("charter_capital")
    private String charterCapital;

    @JsonProperty("business")
    private String business;

    @JsonProperty("deleted")
    private String deleted;

    @JsonProperty("rep_name")
    private String repName;

    @JsonProperty("rep_position")
    private String repPosition;

    @JsonProperty("rep_card_no")
    private String repCardNo;

    @JsonProperty("rep_card_date")
    private LocalDate repCardDate;

    @JsonProperty("rep_card_issuer")
    private String repCardIssuer;

    @JsonProperty("links")
    private String links;

    @JsonProperty("content")
    private String content;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("description")
    private String description;
}

