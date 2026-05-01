package com.rodriguez.inventorysales.exception;

public class ConcurrencyConflictException extends RuntimeException {
    public ConcurrencyConflictException(String msg) { super(msg); }
}