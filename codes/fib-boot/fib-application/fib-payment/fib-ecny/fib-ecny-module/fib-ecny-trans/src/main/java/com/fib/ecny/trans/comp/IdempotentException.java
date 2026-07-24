package com.fib.ecny.trans.comp;

public class IdempotentException extends RuntimeException {
    public IdempotentException(String message) {
        super(message);
    }
}
