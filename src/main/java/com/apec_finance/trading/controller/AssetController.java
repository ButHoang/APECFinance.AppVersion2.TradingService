package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.Asset;
import com.apec_finance.trading.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @PostMapping("/get")
    public ResponseBuilder<Asset> getAsset(Long accountId) {
        Asset rs = assetService.getTotalAsset(accountId);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
