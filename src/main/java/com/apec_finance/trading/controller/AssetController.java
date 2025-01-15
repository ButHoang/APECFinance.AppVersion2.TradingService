package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.asset.Asset;
import com.apec_finance.trading.model.asset.AssetRS;
import com.apec_finance.trading.model.asset.SearchAssetRQ;
import com.apec_finance.trading.model.interest.Interest;
import com.apec_finance.trading.service.AssetService;
import com.apec_finance.trading.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;
    @PostMapping("/list")
    public ResponseBuilder<AssetRS> getListAsset(@RequestBody(required = false) SearchAssetRQ searchAssetRQ, KeycloakService keycloakService) {
        var rs = assetService.getListAsset(searchAssetRQ, keycloakService.getInvestorIdFromToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
