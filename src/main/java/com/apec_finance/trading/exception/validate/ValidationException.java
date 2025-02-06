package com.apec_finance.trading.exception.validate;

import com.apec_finance.trading.comon.CommonErrorCodes;
import com.apec_finance.trading.exception.base_exception.CustomException;

public class ValidationException extends CustomException {
    public ValidationException(String message) {
        super(CommonErrorCodes.VALIDATION_ERROR, message);
    }
}
