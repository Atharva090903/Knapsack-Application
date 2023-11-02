package com.example.daapbl;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private EditText edtKnapsackCapacity, edtItemWeight, edtItemValue;
    private Button btnAddItem, btnCalculate;
    private TextView txtResult;

    private int knapsackCapacity;
    private ArrayList<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edtKnapsackCapacity = findViewById(R.id.edtKnapsackCapacity);
        edtItemWeight = findViewById(R.id.edtItemWeight);
        edtItemValue = findViewById(R.id.edtItemValue);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnCalculate = findViewById(R.id.btnCalculate);
        txtResult = findViewById(R.id.txtResult);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateKnapsack();
            }
        });
    }

    private void addItem() {
        try {
            int weight = Integer.parseInt(edtItemWeight.getText().toString());
            int value = Integer.parseInt(edtItemValue.getText().toString());

            items.add(new Item(weight, value));
            Toast.makeText(MainActivity2.this, "Item added (Weight: " + weight + ", Value: " + value + ")", Toast.LENGTH_SHORT).show();

            edtItemWeight.getText().clear();
            edtItemValue.getText().clear();
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity2.this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateKnapsack() {
        try {
            knapsackCapacity = Integer.parseInt(edtKnapsackCapacity.getText().toString());

            if (!items.isEmpty()) {
                int numItems = items.size();
                int[][] dp = new int[numItems + 1][knapsackCapacity + 1];

                for (int i = 1; i <= numItems; i++) {
                    for (int w = 0; w <= knapsackCapacity; w++) {
                        if (items.get(i - 1).weight <= w) {
                            dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - items.get(i - 1).weight] + items.get(i - 1).value);
                        } else {
                            dp[i][w] = dp[i - 1][w];
                        }
                    }
                }

                // Calculate the maximum value
                int maxValue = dp[numItems][knapsackCapacity];

                // Display the selected items and total value
                StringBuilder resultBuilder = new StringBuilder();
                resultBuilder.append("Selected Items:\n");
                for (int i = numItems, w = knapsackCapacity; i > 0; i--) {
                    if (dp[i][w] != dp[i - 1][w]) {
                        Item selectedItem = items.get(i - 1);
                        resultBuilder.append("Item [Weight: ")
                                .append(selectedItem.weight)
                                .append(", Value: ")
                                .append(selectedItem.value)
                                .append("]\n");
                        w -= selectedItem.weight;
                    }
                }

                resultBuilder.append("Total Value: ").append(maxValue);

                // Display the result in a TextView
                txtResult.setText(resultBuilder.toString());

                // Clear the list of items
                items.clear();

            } else {
                txtResult.setText("No items added.");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity2.this, "Invalid knapsack capacity", Toast.LENGTH_SHORT).show();
        }
    }

}
