package com.emplk.realestatemanager.initial_bugfix;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.emplk.realestatemanager.R;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMain = findViewById(R.id.activity_main_activity_text_view_main);  // fixed wrong id
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        configureTextViewMain();
        configureTextViewQuantity();
    }

    private void configureTextViewMain() {
        textViewMain.setTextSize(15);
        textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        textViewQuantity.setTextSize(20);
        textViewQuantity.setText(String.valueOf(quantity)); // fixed value int to String representation
    }
}