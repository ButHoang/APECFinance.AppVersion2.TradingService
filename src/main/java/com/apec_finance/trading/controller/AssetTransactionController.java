package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.asset.AssetRS;
import com.apec_finance.trading.model.asset.SearchAssetRQ;
import com.apec_finance.trading.model.asset_transaction.AssetTransaction;
import com.apec_finance.trading.model.asset_transaction.SearchAssetTransactionRQ;
import com.apec_finance.trading.service.AssetService;
import com.apec_finance.trading.service.InvestorAssetTransactionService;
import com.apec_finance.trading.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asset/transaction")
@RequiredArgsConstructor
public class AssetTransactionController {
    private final InvestorAssetTransactionService assetTransactionService;
    @PostMapping("/list")
    public ResponseBuilder<PaginationRS<AssetTransaction>> getListAssetTransaction(@RequestBody(required = false) SearchAssetTransactionRQ rq) {
        var rs = assetTransactionService.getListAssetTransaction(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
