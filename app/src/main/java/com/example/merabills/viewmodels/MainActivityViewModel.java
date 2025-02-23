package com.example.merabills.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.merabills.enums.PaymentMode;
import com.example.merabills.models.Payment;
import com.example.merabills.utils.PaymentsFileHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    public List<Payment>  payments= new ArrayList<>();
    private final MutableLiveData<Float> _totalAmount = new MutableLiveData<Float>(0.0F);
    public LiveData<Float> totalAmount = _totalAmount;

    public MainActivityViewModel(Application application){
        super(application);
        float totalAmount = 0.0f;
        List<Payment> savedPayments = PaymentsFileHandler.readPaymentsFromFile(getApplication());;
        for(Payment payment: savedPayments){
            payments.add(payment);
            totalAmount += payment.amount;
        }

        this._totalAmount.setValue(totalAmount);
    }
    public ArrayList<PaymentMode> getRemainingPaymentModeOptions(){
        ArrayList<PaymentMode> modes = new ArrayList<>(Arrays.asList(PaymentMode.CASH, PaymentMode.BANK_TRANSFER, PaymentMode.CREDIT_CARD));
        for(Payment payment: payments){
            modes.remove(payment.mode);
        }
        return modes;
    }

    public void addNewPayment(Payment payment){
        payments.add(payment);
        _totalAmount.setValue(_totalAmount.getValue() + payment.amount);
    }

    public void removePayment(int position){
        _totalAmount.setValue(_totalAmount.getValue() - payments.get(position).amount);
        payments.remove(position);
    }

    public void savePayments(){
        PaymentsFileHandler.savePaymentsToFile(getApplication(), payments);
    }
}