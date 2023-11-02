package com.example.daapbl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtNumItems, edtKnapsackCapacity, edtItemWeight, edtItemValue;
    private Button btnAddItem, btnCalculate;

    private List<Item> items;

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNumItems = findViewById(R.id.edtNumItems);
        edtKnapsackCapacity = findViewById(R.id.edtKnapsackCapacity);
        edtItemWeight = findViewById(R.id.edtItemWeight);
        edtItemValue = findViewById(R.id.edtItemValue);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnCalculate = findViewById(R.id.btnCalculate);
        items = new ArrayList<>();

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });



        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    calculateKnapsack();

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void openActivity(View v){
        Toast.makeText(MainActivity.this, "Switching to 0/1 Knapsack", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(this,MainActivity2.class);
        startActivity(intent);
    }

        private void addItem() {
            try {
                int weight = Integer.parseInt(edtItemWeight.getText().toString());
                int value = Integer.parseInt(edtItemValue.getText().toString());
                int numItems= Integer.parseInt(edtNumItems.getText().toString());
                if (items.size() < numItems) {
                    items.add(new Item(weight, value));
                    Toast.makeText(MainActivity.this, "Item added "+"{weight="+weight+" Value="+value+"}", Toast.LENGTH_SHORT).show();
                } else {
                    // Notify the user that they can't add more items
                    Toast.makeText(this, "limit reached", Toast.LENGTH_SHORT).show();
                }
//                items.add(new Item(weight, value));
                // Clear input fields
                edtItemWeight.getText().clear();
                edtItemValue.getText().clear();
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }

    private void calculateKnapsack() {
        int numItems = items.size(); // Get the number of items
        int capacity = Integer.parseInt(edtKnapsackCapacity.getText().toString());

        // Sort items in descending order of value-to-weight ratio (value per unit weight)
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                double ratio1 = (double) item1.value / item1.weight;
                double ratio2 = (double) item2.value / item2.weight;
                if (ratio1 > ratio2) {
                    return -1;
                } else if (ratio1 < ratio2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        double totalValue = 0.0;
        for (Item item : items) {
            if (capacity >= item.weight) {
                // Take the whole item if it fits
                capacity -= item.weight;
                totalValue += item.value;
            } else {
                // Take a fraction of the item that fits
                double fraction = (double) capacity / item.weight;
                totalValue += fraction * item.value;
                break;  // Knapsack is full
            }
        }


        // Display the selected items and total value
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("Selected Items:\n");
        for (Item item : items) {
            resultBuilder.append("Item [Weight: ").append(item.weight).append(", Value: ").append(item.value).append("]\n");
        }
        resultBuilder.append("Total Value: ").append(totalValue);

        // Display the result in a TextView
         TextView txtResult = findViewById(R.id.txtResult);
         txtResult.setText(resultBuilder.toString());
         items.clear();
    }
    }


class Item {
    int weight;
    int value;

    public Item(int weight, int value) {
        this.weight = weight;
        this.value = value;
    }
}
