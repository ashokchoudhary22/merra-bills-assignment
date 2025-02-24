package com.example.merabills.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.merabills.databinding.LayoutDialogAddPaymentBinding;
import com.example.merabills.enums.PaymentMode;
import com.example.merabills.models.Payment;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AddPaymentDialog extends Dialog {

    private static final double AMOUNT_LIMIT = 100_00_00_000;

    private LayoutDialogAddPaymentBinding binding;
    private final PaymentConfirmationListener paymentConfirmationListener;
    private final ArrayList<PaymentMode> availablePaymentModes;
    private PaymentMode selectedPaymentMode;

    public interface PaymentConfirmationListener {
        void onConfirm(Payment payment);
    }

    public AddPaymentDialog(@NonNull Context context, ArrayList<PaymentMode> availablePaymentModes, PaymentConfirmationListener paymentConfirmationListener) {
        super(context);
        this.paymentConfirmationListener = paymentConfirmationListener;
        this.availablePaymentModes = availablePaymentModes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LayoutDialogAddPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getWindow() != null) {
            int width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.94);
            getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setupPaymentModeSpinner();
        setListeners();
    }

    private void setListeners(){
       setPaymentModeSpinnerListener();
       setActionButtonListeners();
       setAmountTextWatcher();
    }

    private void setupPaymentModeSpinner(){
        ArrayList<String> paymentMethodVisibleNames = new ArrayList<>();
        for(PaymentMode mode: availablePaymentModes){
            paymentMethodVisibleNames.add(mode.getVisibleName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, paymentMethodVisibleNames);
        binding.modeSpinner.setAdapter(adapter);
    }

    private void setPaymentModeSpinnerListener(){
        binding.modeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMode = availablePaymentModes.get(position);
                updateTransactionDetailsUIVisibility();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }
    private void setActionButtonListeners(){
        binding.okBt.setOnClickListener(v -> {
            onPaymentConfirmation();
        });

        binding.cancelTv.setOnClickListener(v -> dismiss());
    }

    private void setAmountTextWatcher(){
        binding.amountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(".")) {
                    binding.amountEt.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void updateTransactionDetailsUIVisibility(){
        if(selectedPaymentMode == PaymentMode.CASH){
            binding.providerEt.setVisibility(View.INVISIBLE);
            binding.transactionReferenceEt.setVisibility(View.INVISIBLE);
        } else {
            binding.providerEt.setVisibility(View.VISIBLE);
            binding.transactionReferenceEt.setVisibility(View.VISIBLE);
        }
    }

    private void onPaymentConfirmation(){
        String amountStr = binding.amountEt.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(getContext(), "Enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        if(amount > AMOUNT_LIMIT){
            Toast.makeText(getContext(), "Amount should not be greater than " + (new BigDecimal(AMOUNT_LIMIT)).toPlainString(), Toast.LENGTH_SHORT).show();
            return;
        }

        if(selectedPaymentMode == null){
            Toast.makeText(getContext(), "select Payment mode", Toast.LENGTH_SHORT).show();
            return;
        }

        String provider = binding.providerEt.getText().toString().trim();
        if(provider.isBlank())
            provider = null;
        String reference = binding.transactionReferenceEt.getText().toString().trim();
        if ((reference.isBlank()))
            reference = null;

        Payment payment = new Payment(amount, selectedPaymentMode, provider, reference);

        paymentConfirmationListener.onConfirm(payment);
        dismiss();
    }
}
