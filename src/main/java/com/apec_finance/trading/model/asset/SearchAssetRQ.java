package com.apec_finance.trading.model.asset;

import com.apec_finance.trading.comon.BaseSearch;
import lombok.Data;

import java.util.List;

@Data
public class SearchAssetRQ extends BaseSearch {
    private List<Integer> productIds;
}
