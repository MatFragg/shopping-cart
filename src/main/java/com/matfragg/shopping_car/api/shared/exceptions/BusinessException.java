package com.matfragg.shopping_car.api.shared.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String authenticationRequired, HttpStatus httpStatus) {
    }
}
