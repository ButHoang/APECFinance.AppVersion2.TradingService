package com.apec_finance.trading.model.order;

import com.apec_finance.trading.model.order.AppProduct;
import lombok.Data;

import java.util.List;

@Data
public class AppStockRS {
    private List<AppProduct> data;
}
