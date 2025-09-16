package com.maconde.exception;

import java.io.Serial;

public class CompanyNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 5219577981995542003L;

    public CompanyNotFoundException(String message) {
        super(message);
    }
}
