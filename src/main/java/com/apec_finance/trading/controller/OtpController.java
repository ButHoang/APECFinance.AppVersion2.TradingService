package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.otp.CheckOtpRQ;
import com.apec_finance.trading.model.otp.GenOtpRQ;
import com.apec_finance.trading.service.KeycloakService;
import com.apec_finance.trading.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;
    @PostMapping("/get")
    public ResponseBuilder<String> genOtpAndSendToInvestor(@RequestBody GenOtpRQ phone, KeycloakService keycloakService) {
        var rs = otpService.getOtp(phone, keycloakService.getInvestorIdFromToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
    @PostMapping("/check")
    public ResponseBuilder<Boolean> checkOtp(@Valid @RequestBody CheckOtpRQ checkOtpRQ, KeycloakService keycloakService) {
        var rs = otpService.validateOtp(checkOtpRQ, keycloakService.getInvestorIdFromToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
