package com.example.budgetplanner;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.budgetplanner.Adapter.TransactionAdapter;
import com.example.budgetplanner.Models.Transaction;
import com.example.budgetplanner.databinding.ActivityAllTransactionsViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AllTransactionsViewActivity extends AppCompatActivity {

    ActivityAllTransactionsViewBinding binding;

    FirebaseDatabase database;
    FirebaseAuth auth;

    ArrayList<Transaction> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllTransactionsViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        TransactionAdapter adapter = new TransactionAdapter(list, this);
        binding.allTransactionsRV.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.allTransactionsRV.setLayoutManager(layoutManager);

        binding.allTransactionsRV.addItemDecoration(new DividerItemDecoration(binding.allTransactionsRV.getContext(), DividerItemDecoration.VERTICAL));

        database.getReference().child("Transaction").child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Transaction event = dataSnapshot.getValue(Transaction.class);
                            list.add(event);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}