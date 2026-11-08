
package com.example.baggagewise;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.Intent;

import java.util.Locale;

public class MainActivity extends Activity {

    EditText etWeight, etRate;
    Spinner spinnerClass;

    Button btnCalculate, btnReset, btnAbout;
    ImageButton btnShare;

    TextView tvExcessWeight, tvCharge, tvTotal, tvError;

    String appUrl = "https://github.com/nrsyzaaaa/BaggageWise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect XML components
        etWeight = findViewById(R.id.etWeight);
        etRate = findViewById(R.id.etRate);

        spinnerClass = findViewById(R.id.spinnerClass);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        btnShare = findViewById(R.id.btnShare);
        btnAbout = findViewById(R.id.btnAbout);

        tvExcessWeight = findViewById(R.id.tvExcessWeight);
        tvCharge = findViewById(R.id.tvCharge);
        tvTotal = findViewById(R.id.tvTotal);
        tvError = findViewById(R.id.tvError);

        // Ticket class options
        String[] classes = {"Economy", "Business"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        classes
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerClass.setAdapter(adapter);

        // Calculate button
        btnCalculate.setOnClickListener(v -> calculateFee());

        // Reset button
        btnReset.setOnClickListener(v -> resetCalculator());

        // Share icon in header
        btnShare.setOnClickListener(v -> shareApplication());

        // About button at the bottom
        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AboutActivity.class
            );
            startActivity(intent);
        });
    }

    private void calculateFee() {

        String weightText = etWeight.getText().toString().trim();
        String rateText = etRate.getText().toString().trim();

        // Check empty input
        if (weightText.isEmpty()) {
            showError("Please enter your baggage weight.");
            etWeight.requestFocus();
            return;
        }

        if (rateText.isEmpty()) {
            showError("Please enter the excess baggage rate.");
            etRate.requestFocus();
            return;
        }

        double weight;
        double rate;

        try {
            weight = Double.parseDouble(weightText);
            rate = Double.parseDouble(rateText);

        } catch (NumberFormatException e) {
            showError("Please enter valid numerical values.");
            return;
        }

        // Validate numerical values
        if (!Double.isFinite(weight) || !Double.isFinite(rate)) {
            showError("Please enter valid numerical values.");
            return;
        }

        // Validate negative values
        if (weight < 0) {
            showError("Baggage weight cannot be negative.");
            etWeight.requestFocus();
            return;
        }

        if (rate < 0) {
            showError("Rate cannot be negative.");
            etRate.requestFocus();
            return;
        }

        // Determine baggage allowance
        String ticketClass =
                spinnerClass.getSelectedItem().toString();

        double allowance;

        if (ticketClass.equals("Economy")) {
            allowance = 20;
        } else {
            allowance = 40;
        }

        // Calculate excess baggage weight
        double excessWeight =
                Math.max(0, weight - allowance);

        // Calculate excess baggage charge
        double excessCharge =
                excessWeight * rate;

        // Calculate 8% service tax
        double serviceTax =
                excessCharge * 0.08;

        // Calculate total payable
        double totalPayable =
                excessCharge + serviceTax;

        // Display results
        tvExcessWeight.setText(
                String.format(
                        Locale.getDefault(),
                        "Excess Weight: %.2f kg",
                        excessWeight
                )
        );

        tvCharge.setText(
                String.format(
                        Locale.getDefault(),
                        "Excess Charge: RM %.2f",
                        excessCharge
                )
        );

        tvTotal.setText(
                String.format(
                        Locale.getDefault(),
                        "Total Payable: RM %.2f",
                        totalPayable
                )
        );

        // Hide error message
        tvError.setVisibility(View.GONE);
    }

    private void showError(String message) {

        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    private void resetCalculator() {

        etWeight.setText("");
        etRate.setText("");

        spinnerClass.setSelection(0);

        tvExcessWeight.setText("Excess Weight: -- kg");
        tvCharge.setText("Excess Charge: RM --");
        tvTotal.setText("Total Payable: RM --");

        tvError.setVisibility(View.GONE);
    }

    private void shareApplication() {

        Intent shareIntent =
                new Intent(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");

        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Try BaggageWise - Smart Excess Baggage Calculator!\n\n"
                        + appUrl
        );

        startActivity(
                Intent.createChooser(
                        shareIntent,
                        "Share BaggageWise"
                )
        );
    }
}
