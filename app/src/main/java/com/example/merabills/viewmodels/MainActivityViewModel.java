package com.example.merabills.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.merabills.enums.PaymentMode;
import com.example.merabills.models.Payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    public List<Payment>  payments= new ArrayList<>();
    public MutableLiveData<Float> totalAmount = new MutableLiveData<Float>(0.0F);

    public ArrayList<PaymentMode> getRemainingPaymentModeOptions(){
        ArrayList<PaymentMode> modes = new ArrayList<>(Arrays.asList(PaymentMode.CASH, PaymentMode.BANK_TRANSFER, PaymentMode.CREDIT_CARD));
        for(Payment payment: payments){
            modes.remove(payment.mode);
        }
        return modes;
    }

    public void addNewPayment(Payment payment){
        payments.add(payment);
        totalAmount.setValue(totalAmount.getValue() + payment.amount);
    }

    public void removePayment(int position){
        totalAmount.setValue(totalAmount.getValue() - payments.get(position).amount);
        payments.remove(position);
    }
}