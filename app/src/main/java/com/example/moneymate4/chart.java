package com.example.moneymate4;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Color;




import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;;
import java.util.List;


public class chart extends AppCompatActivity {


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
        setContentView(R.layout.activity_chart);


        amountEditText = findViewById(R.id.amountEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        addButton = findViewById(R.id.addButton);
        pieChart = findViewById(R.id.pieChart);

        pieEntries = new ArrayList<>();
        setupPieChart();

        // Set up spinner with array adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAmountToTable();
            }
        });





    }


    private void setupPieChart() {
        // Initialize PieDataSet with default color white for all sectors
        pieEntries.add(new PieEntry(100, "")); // Start with one sector for demo

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
        String amountStr = amountEditText.getText().toString().trim();
        if (!amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);
            String type = typeSpinner.getSelectedItem().toString();

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
            Toast.makeText(chart.this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(chart.this, "Enter amount", Toast.LENGTH_SHORT).show();
        }
    }

    private int getRandomColor() {
        // Generate a random color
        return Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }



}




