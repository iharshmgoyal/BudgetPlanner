package com.example.budgetplanner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetplanner.Models.Transaction;
import com.example.budgetplanner.R;
import com.example.budgetplanner.databinding.TransactionViewRvBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.viewHolder> {

    ArrayList<Transaction> list;
    Context context;

    public TransactionAdapter(ArrayList<Transaction> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_view_rv,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.viewHolder holder, int position) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM-dd-yyyy HH:mm:ss");

        Transaction transaction = list.get(position);

        Date date = new Date(transaction.getTransactionCreatedAt());

        String createdAtDate = dateFormat.format(date);

        holder.transactionName.setText(transaction.getTransactionName());
        holder.transactionAmount.setText(transaction.getTransactionAmount());
        holder.transactionAccount.setText(transaction.getTransactionAccount());
        holder.lastBalance.setText(String.format("Rs. %s", transaction.getLastBalance()));
        holder.occurredAt.setText(createdAtDate);

        if (transaction.getTransactionAccount().equalsIgnoreCase("icici bank")) {
            holder.accountTypeImageView.setImageResource(R.drawable.icici);
        } else if (transaction.getTransactionAccount().equalsIgnoreCase("kotak mahindra bank")) {
            holder.accountTypeImageView.setImageResource(R.drawable.kotak);
        } else if (transaction.getTransactionAccount().equalsIgnoreCase("sbi bank")) {
            holder.accountTypeImageView.setImageResource(R.drawable.sbi);
        } else if (transaction.getTransactionAccount().equalsIgnoreCase("icici - credit card")) {
            holder.accountTypeImageView.setImageResource(R.drawable.icici_cc);
        } else if (transaction.getTransactionAccount().equalsIgnoreCase("kotak mahindra credit card")) {
            holder.accountTypeImageView.setImageResource(R.drawable.kotak_cc);
        } else if (transaction.getTransactionAccount().equalsIgnoreCase("hdfc credit card")) {
            holder.accountTypeImageView.setImageResource(R.drawable.hdfc);
        } else {
            holder.accountTypeImageView.setImageResource(R.drawable.default_img);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TransactionViewRvBinding binding;

        TextView transactionName, transactionAmount, transactionAccount, lastBalance, occurredAt;
        ImageView accountTypeImageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=TransactionViewRvBinding.bind(itemView);

            lastBalance=binding.balance;
            transactionName=binding.eventName;
            transactionAmount=binding.amount;
            transactionAccount=binding.account;
            occurredAt=binding.occurredAt;
            accountTypeImageView=binding.accountTypeImagevIew;

        }
    }
}