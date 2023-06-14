package com.example.budgetplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.budgetplanner.Models.Transaction;
import com.example.budgetplanner.databinding.ActivityAddEventBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity {

    ActivityAddEventBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String transReason, transType, transAccount;
    long lastBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

//      Spinners
        // Event Reason Spinner
        ArrayAdapter<CharSequence> eventReasonAdapter = ArrayAdapter.createFromResource(this, R.array.event_reason_select, android.R.layout.simple_spinner_item);
        eventReasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.eventReasonSpinner.setAdapter(eventReasonAdapter);

        binding.eventReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if (selectedItem.matches("Select Reason of Event")) {
//                    Toast.makeText(AddNewEventActivity.this, "Please select the reason of Event!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "onItemSelected: Selected Reason of Event : " + selectedItem);
                    transReason = selectedItem;
                    Toast.makeText(adapterView.getContext(), selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddEventActivity.this, "Select proper reason of transaction!!", Toast.LENGTH_SHORT).show();
            }
        });

        // Event Type Spinner
        ArrayAdapter<CharSequence> eventTypeAdapter = ArrayAdapter.createFromResource(this, R.array.event_type_select, android.R.layout.simple_spinner_item);
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.eventTypeSpinner.setAdapter(eventTypeAdapter);

        binding.eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if (selectedItem.matches("Select Type of Event")) {
//                    Toast.makeText(AddNewEventActivity.this, "Please select the reason of Event!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "onItemSelected: Selected Type of Event : " + selectedItem);
                    transType = selectedItem;
                    Toast.makeText(adapterView.getContext(), selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddEventActivity.this, "Select proper type of transaction!!", Toast.LENGTH_SHORT).show();
            }
        });

        // Account Type Spinner
        ArrayAdapter<CharSequence> accountSpinner = ArrayAdapter.createFromResource(this, R.array.account_select, android.R.layout.simple_spinner_item);
        accountSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.accountSelectorSpinner.setAdapter(accountSpinner);

        binding.accountSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if (selectedItem.matches("Select Account")) {
//                    Toast.makeText(AddNewEventActivity.this, "Please select the reason of Event!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "onItemSelected: Selected Account : " + selectedItem);
                    transAccount = selectedItem;
                    Toast.makeText(adapterView.getContext(), selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AddEventActivity.this, "Select proper transaction Account!!", Toast.LENGTH_SHORT).show();
            }
        });


//        Buttons Clicked
        binding.createEventButton.setOnClickListener(v -> createButtonClicked());
        binding.createAndAddNewButton.setOnClickListener(v -> createdAndAddNewButtonClicked());

    }

    private void getTransactionAccountLastBalance(String transAccount) {
        //        Balance Update Code

        database.getReference().child("Transaction").child(auth.getUid()).child(transAccount).orderByKey().limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            lastBalance = Long.parseLong(dataSnapshot.child("transactionAmount").getValue().toString());
                            Log.d("TAG", "onDataChange: The Last Updated Balance for "+ transAccount +" is : "+lastBalance);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void createButtonClicked() {

        String transName, transDesc, transAmount;

        transName= Objects.requireNonNull(binding.eventNameEditText.getText()).toString();
        transDesc= Objects.requireNonNull(binding.eventDescEditText.getText()).toString();
        transAmount= Objects.requireNonNull(binding.eventAmountEditText.getText()).toString();

        if (!transName.equals("") && !transDesc.equals("") && !transAmount.equals("")){
            if ((transReason!=null || !transReason.equalsIgnoreCase("Select Reason of Event"))
                    && (transType!=null || !transType.equalsIgnoreCase("Select type of Event"))
                    && (transAccount!=null || !transAccount.equalsIgnoreCase("select account"))) {
                Log.d("TAG", "onClick: All Fields are correct not inserting the transaction into Database");

                if (String.valueOf(lastBalance)!=null) {
                    if (transType.equalsIgnoreCase("add")) {

                        getTransactionAccountLastBalance(transAccount);

                        Transaction transaction = new Transaction(
                                transName,
                                transReason,
                                transDesc,
                                transType,
                                transAccount,
                                transAmount,
                                auth.getUid(),
                                String.valueOf(lastBalance+Long.parseLong(transAmount)),
                                new Date().getTime()
                        );

                        database.getReference()
                                .child("Transaction")
                                .child(auth.getUid())
                                .child(transAccount)
                                .push()
                                .setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AddEventActivity.this, "Transaction added successfully!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                                        overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddEventActivity.this, "Transaction addition Failed!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else if (transType.equalsIgnoreCase("subtract")) {

                        getTransactionAccountLastBalance(transAccount);

                        Log.d("TAG", "createButtonClicked: Last Balance received when subtract is clicked = "+lastBalance);

                        Transaction transaction = new Transaction(
                                transName,
                                transReason,
                                transDesc,
                                transType,
                                transAccount,
                                transAmount,
                                auth.getUid(),
                                String.valueOf(lastBalance-Long.parseLong(transAmount)),
                                new Date().getTime()
                        );

                        database.getReference()
                                .child("Transaction")
                                .child(auth.getUid())
                                .child(transAccount)
                                .push()
                                .setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AddEventActivity.this, "Transaction added successfully!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                                        overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddEventActivity.this, "Transaction addition Failed!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {

                    getTransactionAccountLastBalance(transAccount);

                    Transaction transaction = new Transaction(
                            transName,
                            transReason,
                            transDesc,
                            transType,
                            transAccount,
                            transAmount,
                            auth.getUid(),
                            transAmount,
                            new Date().getTime()
                    );

                    database.getReference()
                            .child("Transaction")
                            .child(auth.getUid())
                            .child(transAccount)
                            .push()
                            .setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddEventActivity.this, "Transaction added successfully!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                                    overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddEventActivity.this, "Transaction addition Failed!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        }

    }

    private void createdAndAddNewButtonClicked() {
        String transName, transDesc, transAmount;

        transName = Objects.requireNonNull(binding.eventNameEditText.getText()).toString();
        transDesc = Objects.requireNonNull(binding.eventDescEditText.getText()).toString();
        transAmount = Objects.requireNonNull(binding.eventAmountEditText.getText()).toString();

        if (!transName.equals("") && !transDesc.equals("") && !transAmount.equals("")) {
            if ((transReason != null || !transReason.equalsIgnoreCase("Select Reason of Event"))
                    && (transType != null || !transType.equalsIgnoreCase("Select type of Event"))
                    && (transAccount != null || !transAccount.equalsIgnoreCase("select account"))) {
                Log.d("TAG", "onClick: All Fields are correct not inserting the transaction into Database");

                if (String.valueOf(lastBalance)!=null) {
                    if (transType.equalsIgnoreCase("add")) {
                        Transaction transaction = new Transaction(
                                transName,
                                transReason,
                                transDesc,
                                transType,
                                transAccount,
                                transAmount,
                                auth.getUid(),
                                String.valueOf(lastBalance+Long.parseLong(transAmount)),
                                new Date().getTime()
                        );

                        database.getReference()
                                .child("Transaction")
                                .child(auth.getUid())
                                .child(transAccount)
                                .push()
                                .setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AddEventActivity.this, "Transaction added successfully!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddEventActivity.this, "Transaction addition Failed!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else if (transType.equalsIgnoreCase("subtract")) {
                        Transaction transaction = new Transaction(
                                transName,
                                transReason,
                                transDesc,
                                transType,
                                transAccount,
                                transAmount,
                                auth.getUid(),
                                String.valueOf(lastBalance-Long.parseLong(transAmount)),
                                new Date().getTime()
                        );

                        database.getReference()
                                .child("Transaction")
                                .child(auth.getUid())
                                .child(transAccount)
                                .push()
                                .setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AddEventActivity.this, "Transaction added successfully!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddEventActivity.this, "Transaction addition Failed!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Transaction transaction = new Transaction(
                            transName,
                            transReason,
                            transDesc,
                            transType,
                            transAccount,
                            transAmount,
                            auth.getUid(),
                            transAmount,
                            new Date().getTime()
                    );

                    database.getReference()
                            .child("Transaction")
                            .child(auth.getUid())
                            .child(transAccount)
                            .push()
                            .setValue(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddEventActivity.this, "Transaction added successfully!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddEventActivity.this, "Transaction addition Failed!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        }
    }
}