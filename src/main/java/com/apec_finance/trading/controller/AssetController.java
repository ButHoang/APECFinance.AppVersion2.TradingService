package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.asset.*;
import com.apec_finance.trading.service.AssetService;
import com.apec_finance.trading.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;
    @PostMapping("/list")
    public ResponseBuilder<AssetRS> getListAsset(@RequestBody(required = false) @Valid SearchAssetRQ searchAssetRQ, KeycloakService keycloakService) {
        var rs = assetService.getListAsset(searchAssetRQ, keycloakService.getInvestorIdFromToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/order")
    public ResponseBuilder<AssetOrderInfo> getListAssetAndOrderInfo(@RequestParam String assetNo) {
        var rs = assetService.getAssetAndOrderInfo(assetNo);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/product")
    public ResponseBuilder<AssetProduct> getAssetProductInfo(@RequestParam List<Integer> productIds, KeycloakService keycloakService) {
        var rs = assetService.getAssetAndProductInfo(keycloakService.getInvestorIdFromToken(), productIds);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @GetMapping("/stats")
    public ResponseBuilder<AssetStats> getAssetStats(@RequestParam Integer status, KeycloakService keycloakService) {
        var rs = assetService.getAssetStats(keycloakService.getInvestorIdFromToken(), status);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
