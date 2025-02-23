package com.example.merabills.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merabills.R;
import com.example.merabills.databinding.ItemPaymentsBinding;
import com.example.merabills.models.Payment;
import java.util.List;

public class PaymentsRecyclerViewAdapter extends RecyclerView.Adapter<PaymentsRecyclerViewAdapter.PaymentViewHolder> {

    private final List<Payment> paymentList;
    private final OnCancelIconClickListener listener;

    public PaymentsRecyclerViewAdapter(List<Payment> paymentList, OnCancelIconClickListener listener) {
        this.paymentList = paymentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPaymentsBinding binding = ItemPaymentsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaymentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        String paymentText = holder.itemView.getContext().getString(R.string.payment_mode_amount,payment.mode.getVisibleName(), payment.amount);
        holder.binding.paymentMethodText.setText(paymentText);
        holder.binding.removeIcon.setOnClickListener(v -> listener.onClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public interface OnCancelIconClickListener {
        void onClick(int position);
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private final ItemPaymentsBinding binding;

        public PaymentViewHolder(ItemPaymentsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
