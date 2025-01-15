package com.apec_finance.trading.model.order;

import com.apec_finance.trading.model.TransactionRange;
import lombok.Data;

import java.util.List;

@Data
public class TransactionRangeRS {
    private String error;
    private String errorReason;
    private String toastMessage;
    private List<TransactionRange> result;
}
