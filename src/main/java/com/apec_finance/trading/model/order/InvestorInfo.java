package com.apec_finance.trading.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvestorInfo {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("sort")
    private String sort;

    @JsonProperty("user_created")
    private String userCreated;

    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    @JsonProperty("user_updated")
    private String userUpdated;

    @JsonProperty("date_updated")
    private LocalDateTime dateUpdated;

    @JsonProperty("account_no")
    private String accountNo;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("birth_date")
    private String birthDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("card_no")
    private String cardNo;

    @JsonProperty("tax_code")
    private String taxCode;

    @JsonProperty("card_date")
    private String cardDate;

    @JsonProperty("card_issuer")
    private String cardIssuer;

    @JsonProperty("branch_id")
    private Integer branchId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("is_online")
    private String isOnline;

    @JsonProperty("account_status")
    private String accountStatus;

    @JsonProperty("is_econtract_sign")
    private String isEContractSign;

    @JsonProperty("e_contract_url")
    private String eContractUrl;

    @JsonProperty("ec_sign_date")
    private LocalDateTime ecSignDate;

    @JsonProperty("reg_source")
    private String regSource;
}
