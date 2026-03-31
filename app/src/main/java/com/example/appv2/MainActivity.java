package com.example.appv2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerType, spinnerFrom, spinnerTo;
    TextView tvFrom, tvTo, tvValue, tvResultLabel, tvResult;
    EditText etValue;
    Button btnConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerType = findViewById(R.id.spinnerType);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);

        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        tvValue = findViewById(R.id.tvValue);
        tvResultLabel = findViewById(R.id.tvResultLabel);
        tvResult = findViewById(R.id.tvResult);
        etValue = findViewById(R.id.etValue);
        btnConvert = findViewById(R.id.btnConvert);

        String[] types = {"Select Type", "Currency",
                "Fuel Efficiency", "Liquid Volume"};

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                types
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideConversionViews();

                if (position == 1) {
                    showCurrencyViews();
                    loadSpinnerData(
                            new String[]{"USD"},
                            new String[]{"USD", "AUD", "EUR", "JPY", "GBP"}
                    );
                } else if (position == 2) {
                    showFuelViews();
                    loadSpinnerData(
                            new String[]{"mpg", "km/L"},
                            new String[]{"mpg", "km/L"}
                    );
                } else if (position == 3) {
                    showVolumeViews();
                    loadSpinnerData(
                            new String[]{"Liters", "Gallons"},
                            new String[]{"Liters", "Gallons"}
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = etValue.getText().toString().trim();

                if (inputText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double inputValue = Double.parseDouble(inputText);
                    if (inputValue < 0) {
                        Toast.makeText(MainActivity.this, "Value cannot be negative", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String fromUnit = spinnerFrom.getSelectedItem().toString();
                    String toUnit = spinnerTo.getSelectedItem().toString();

                    double result = convertValue(fromUnit, toUnit, inputValue);
                    tvResult.setText(String.format("%.2f %s = %.2f %s", inputValue, fromUnit, result, toUnit));

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadSpinnerData(String[] fromItems, String[] toItems) {
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                fromItems
        );
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                toItems
        );
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(fromAdapter);
        spinnerTo.setAdapter(toAdapter);
    }

    private void hideConversionViews() {
        tvFrom.setVisibility(View.GONE);
        spinnerFrom.setVisibility(View.GONE);
        tvTo.setVisibility(View.GONE);
        spinnerTo.setVisibility(View.GONE);
        tvValue.setVisibility(View.GONE);
        etValue.setVisibility(View.GONE);
        btnConvert.setVisibility(View.GONE);
        tvResultLabel.setVisibility(View.GONE);
        tvResult.setVisibility(View.GONE);
    }
    private void showCurrencyViews() {
        tvFrom.setVisibility(View.VISIBLE);
        spinnerFrom.setVisibility(View.VISIBLE);
        tvTo.setVisibility(View.VISIBLE);
        spinnerTo.setVisibility(View.VISIBLE);
        tvValue.setVisibility(View.VISIBLE);
        etValue.setVisibility(View.VISIBLE);
        btnConvert.setVisibility(View.VISIBLE);
        tvResultLabel.setVisibility(View.VISIBLE);
        tvResult.setVisibility(View.VISIBLE);

    }

    private void showFuelViews() {
        showCurrencyViews();
    }

    private void showVolumeViews() {
        showCurrencyViews();
    }

    private void showTemperatureViews() {
        showCurrencyViews();
    }

    private double convertValue(String fromUnit, String toUnit, double inputValue) {
        if (fromUnit.equals(toUnit)) {
            return inputValue; // Identity conversion
        }

        // Currency conversions (all relative to USD)
        if (toUnit.equals("AUD")) return inputValue * 1.55;
        if (toUnit.equals("EUR")) return inputValue * 0.92;
        if (toUnit.equals("JPY")) return inputValue * 148.50;
        if (toUnit.equals("GBP")) return inputValue * 0.78;

        // Fuel efficiency: mpg <-> km/L
        if (fromUnit.equals("mpg") && toUnit.equals("km/L")) {
            return inputValue * 0.425;
        } else if (fromUnit.equals("km/L") && toUnit.equals("mpg")) {
            return inputValue / 0.425;
        }

        // Volume: Gallons <-> Liters
        if (fromUnit.equals("Gallons") && toUnit.equals("Liters")) {
            return inputValue * 3.785;
        } else if (fromUnit.equals("Liters") && toUnit.equals("Gallons")) {
            return inputValue / 3.785;
        }

        return 0.0; // Unknown conversion
    }
}