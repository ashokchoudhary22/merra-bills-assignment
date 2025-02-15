package com.example.merabills.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.merabills.R;
import com.example.merabills.adapters.PaymentsRecyclerViewAdapter;
import com.example.merabills.databinding.ActivityMainBinding;
import com.example.merabills.enums.PaymentMode;
import com.example.merabills.viewmodels.MainActivityViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;
    private PaymentsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);


        PaymentsRecyclerViewAdapter.OnCancelIconClickListener listener = position -> {
            viewModel.removePayment(position);
            adapter.notifyItemRemoved(position);
        };

        adapter = new PaymentsRecyclerViewAdapter(viewModel.payments, listener);
        binding.paymentsRv.setAdapter(adapter);
        binding.paymentsRv.setLayoutManager(new LinearLayoutManager(this));

        binding.addPaymentTv.setOnClickListener(view -> showAddPaymentDialog());

        viewModel.totalAmount.observe(this, amount ->
                binding.totalAmountTv.setText(getString(R.string.float_value, amount))
        );

        binding.saveBt.setOnClickListener(view -> {
            viewModel.savePayments();
        });
    }

    private void showAddPaymentDialog(){
        ArrayList<PaymentMode> availablePaymentModes = viewModel.getRemainingPaymentModeOptions();
        if(availablePaymentModes.isEmpty()){
            Toast.makeText(this, "No available payment mode", Toast.LENGTH_SHORT).show();
            return;
        }

        AddPaymentDialog paymentDialog = new AddPaymentDialog(this, availablePaymentModes, (payment) -> {
            viewModel.addNewPayment(payment);
            adapter.notifyItemInserted(viewModel.payments.size()-1);
        });
        paymentDialog.show();
    }
}