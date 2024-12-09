package com.apec_finance.trading.comon;

import lombok.Data;

@Data
public class ResponseBuilder<T> {
    private int statusCode;
    private String message;
    private T result;

    public ResponseBuilder(int statusCode, String message, T result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }
}
