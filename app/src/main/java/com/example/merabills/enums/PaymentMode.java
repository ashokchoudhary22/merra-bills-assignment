package com.example.merabills.enums;

public enum PaymentMode {
    CREDIT_CARD("Credit Card"),
    BANK_TRANSFER("Bank Transfer"),
    CASH("Cash");

    private final String visibleName;

    PaymentMode(String visibleName) {
        this.visibleName = visibleName;
    }

    public String getVisibleName() {
        return visibleName;
    }
}