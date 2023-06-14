package com.example.budgetplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.budgetplanner.databinding.ActivityMainBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final float[] yData = {10915.0f,6477.8f,47547.0f,4000.f,9000.5f,2000.5f,5000.5f};
    private final String[] xData = {"Study EMI","Refrigerator EMI","Salary Credited","Other","Shopping","Food","Home - Purchase"};

    @SuppressLint("SimpleDateFormat")
    DateFormat currentMonth = new SimpleDateFormat("MMM - yyyy");
    @SuppressLint("SimpleDateFormat")
    DateFormat currentDate = new SimpleDateFormat("EEE, MMM - dd - yyyy");
    Date date = new Date();

    Description description;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("TAG", "onCreate: MainActivity Created");

        binding.currentDayTextView.setText(String.format("Current Date : %s", currentDate.format(date)));

        binding.currentMonthTextView.setText(String.format("Viewing Expenses for : %s", currentMonth.format(date)));

        binding.addTransactionButton.setOnClickListener(v -> {
            Toast.makeText(this, "Adding new Expense", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AddEventActivity.class));
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        });

        binding.viewAllEventButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AllTransactionsViewActivity.class));
            overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
        });

        Objects.requireNonNull(binding.expensePieChart).setHoleRadius(80);
        binding.expensePieChart.setHoleColor(Color.TRANSPARENT);
        description=binding.expensePieChart.getDescription();
        description.setText("Expense Data");
        binding.expensePieChart.setDescription(description);
        binding.expensePieChart.setCenterTextColor(Color.rgb(255, 102, 0));
        binding.expensePieChart.setCenterTextSize(24f);
        binding.expensePieChart.setCenterText("Expenses");

        addExpenseDataInPieChart();

        binding.expensePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int pos1 = e.toString().indexOf("(sum): ");
                String expense=e.toString().substring(pos1+7);

                for(int i = 0; i < yData.length; i++) {
                    if (yData[i] == Float.parseFloat(expense)) {
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1 + 1];
                Toast.makeText(MainActivity.this, "Employee " + employee + "\n" + "Sales: $" + expense + "K", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("TAG", "onStart: App Started");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Wait user is getting created...");
        progressDialog.setCancelable(false);
        if (FirebaseAuth.getInstance().getCurrentUser()==null) {
            progressDialog.show();
            FirebaseAuth.getInstance()
                    .signInAnonymously()
                    .addOnSuccessListener(authResult -> progressDialog.dismiss())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addExpenseDataInPieChart() {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        for (int i = 0; i<yData.length; i++) {
            yEntry.add(new PieEntry(yData[i], i));
        }

        for (int i = 0; i < xData.length; i++) {
            xEntry.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry, "Expenses");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);

        pieDataSet.setColor(Color.rgb(255, 133, 51));

        Legend legend = binding.expensePieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);

        PieData pieData = new PieData(pieDataSet);
        binding.expensePieChart.setData(pieData);
        binding.expensePieChart.invalidate();

    }
}