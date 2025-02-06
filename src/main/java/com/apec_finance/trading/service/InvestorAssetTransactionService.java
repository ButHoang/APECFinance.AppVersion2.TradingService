package com.apec_finance.trading.service;

import com.apec_finance.trading.comon.PaginationRS;
import com.apec_finance.trading.model.asset_transaction.AssetTransaction;
import com.apec_finance.trading.model.asset_transaction.SearchAssetTransactionRQ;

public interface InvestorAssetTransactionService {
    PaginationRS<AssetTransaction> getListAssetTransaction(SearchAssetTransactionRQ rq);
}
