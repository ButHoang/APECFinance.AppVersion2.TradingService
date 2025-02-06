package com.apec_finance.trading.service;

import com.apec_finance.trading.model.otp.CheckOtpRQ;
import com.apec_finance.trading.model.otp.GenOtpRQ;

public interface OtpService {
    String getOtp(GenOtpRQ genOtpRQ, Long investorId);
    boolean validateOtp(CheckOtpRQ otpInput, Long investorId);
}
