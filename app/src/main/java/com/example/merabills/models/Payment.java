package com.example.merabills.models;

import com.example.merabills.enums.PaymentMode;

public class Payment {
    public PaymentMode mode;
    public double amount;
    public String provider;
    public String transactionReference;

    private Payment(){}
    public Payment(double amount, PaymentMode mode, String provider, String transactionReference) {
        this.mode = mode;
        this.amount = amount;
        this.provider = provider;
        this.transactionReference = transactionReference;
    }

}
