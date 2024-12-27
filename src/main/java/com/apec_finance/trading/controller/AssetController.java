package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.asset.Asset;
import com.apec_finance.trading.model.interest.Interest;
import com.apec_finance.trading.service.AssetService;
import com.apec_finance.trading.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;
    @GetMapping("/list")
    public ResponseBuilder<List<Asset>> getListAsset(KeycloakService keycloakService) {
        var rs = assetService.getListAsset(keycloakService.getInvestorIdFromToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
