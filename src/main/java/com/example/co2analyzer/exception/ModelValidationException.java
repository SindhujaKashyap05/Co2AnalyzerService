package com.example.co2analyzer.exception;

public class ModelValidationException extends Exception {
    public ModelValidationException(String message) {
        super(message);
    }

    public ModelValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}



