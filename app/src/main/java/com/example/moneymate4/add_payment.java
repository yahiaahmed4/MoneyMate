package com.example.moneymate4;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_payment extends AppCompatActivity {


    EditText cardnumber, namee, amount;
    Button pay;

    EditText amountEditText;
    Spinner typeSpinner;
    Button addButton;
    PieChart pieChart;
    List<PieEntry> pieEntries;
    PieDataSet dataSet;
    PieData pieData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("users");
        namee = findViewById(R.id.Namee);
        cardnumber = findViewById(R.id.CardNu);
        amount = findViewById(R.id.amount);
        pay = findViewById(R.id.btnPay);

        //trial youssef code
        pieChart = findViewById(R.id.pieChart);
        pieEntries = new ArrayList<>();
        setupPieChart();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), accountinfo.class));
                return true;
            } else if (id == R.id.navigation_addfavourite) {
                startActivity(new Intent(getApplicationContext(), addAccount.class));
                return true;
            } else if (id == R.id.navigation_addpayment) {
                return true;
            }
            return false;
        });


        SharedPreferences sharedPreferences2 = getSharedPreferences("user_data", MODE_PRIVATE);
//            String userName = sharedPreferences2.getString("name", "");
//            String userCardnumber = sharedPreferences2.getString("cardnumber", "");
//            String userPhone = sharedPreferences2.getString("phone", "");
//            String userCvv = sharedPreferences2.getString("cvv", "");
//            String userEmail = sharedPreferences2.getString("email", "");
        String userID = sharedPreferences2.getString("userId", "");



        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount_ = amount.getText().toString();
                String name_ = namee.getText().toString();


                Map<String, Object> data = new HashMap<>();
                String _name = "transactions"+"."+name_;
                data.put(_name, amount_);


                collectionRef.document(userID)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(add_payment.this, "Approved Transaction!", Toast.LENGTH_SHORT).show();

                                namee.setText("");
                                cardnumber.setText("");
                                amount.setText("");

                                // Retrieve the updated document to get the balance
                                collectionRef.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                // Get the value of the "balance" field
                                                String balance = document.getString("balance");

                                                if(balance !=null && amount_ !=null) {
                                                    int balancee = Integer.parseInt(balance);

                                                    // Convert amount to integer
                                                    int amountt = Integer.parseInt(amount_);

                                                    // Calculate new balance
                                                    int new_balance = balancee - amountt;
                                                    String newBalanceStr = Integer.toString(new_balance);


                                                    collectionRef.document(userID).update("balance", newBalanceStr)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("Firestore", "Balance successfully updated to: " + newBalanceStr);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("Firestore", "Error updating balance", e);
                                                                }
                                                            });

                                                }


                                            } else {
                                                Log.d("Firestore", "No such document");
                                            }
                                        } else {
                                            Log.d("Firestore", "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_payment.this, "Declined Transaction!", Toast.LENGTH_SHORT).show();
                            }
                        });




                //trial youssef code
                addAmountToTable();
            }
        });







    }




    //trial for youssef code



    private void setupPieChart() {
        // Initialize PieDataSet with default color white for all sectors
        pieEntries.add(new PieEntry(0, "")); // Start with one sector for demo

        dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(Color.WHITE); // Default color is white for all sectors

        pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false); // Optional: disable labels on chart
        pieChart.setHoleColor(Color.TRANSPARENT); // Make the center of the pie chart transparent
        pieChart.getDescription().setEnabled(false); // Disable description
        pieChart.invalidate(); // Refresh chart
    }

    private void addAmountToTable() {
        String amountStr = amount.getText().toString().trim();
        if (!amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);
            String type = namee.getText().toString();

            // Update pie chart with a new color for each type
            int color = getRandomColor(); // Get a random color
            dataSet.addColor(color);
            pieEntries.add(new PieEntry((float) amount, type));

            // Notify data set changed
            dataSet.notifyDataSetChanged();
            pieData.notifyDataChanged();
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();

            // Here you can add the amount and type to your table or perform any other action
            String message = "Added: $" + amount + " (" + type + ")";
            Toast.makeText(add_payment.this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(add_payment.this, "Enter amount", Toast.LENGTH_SHORT).show();
        }
    }

    private int getRandomColor() {
        // Generate a random color
        return Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }





}