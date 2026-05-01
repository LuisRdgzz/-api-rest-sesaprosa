package com.rodriguez.inventorysales.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String msg) { super(msg); }
}