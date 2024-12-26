package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.interest.Interest;
import com.apec_finance.trading.service.InterestService;
import com.apec_finance.trading.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interest")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;

    @GetMapping("/get")
    public ResponseBuilder<Interest> getInterestInfo (KeycloakService keycloakService) {
        Interest rs = interestService.getInterestInfo(keycloakService.getInvestorIdFromToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
