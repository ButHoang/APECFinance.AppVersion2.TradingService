package com.apec_finance.trading.model.otp;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CheckOtpRQ {
    @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be a 6-digit number")
    private String otpInput;
}
