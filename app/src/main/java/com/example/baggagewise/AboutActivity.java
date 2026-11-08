package com.example.baggagewise;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        getWindow().getDecorView().setBackgroundColor(
                android.graphics.Color.parseColor("#F4F6FA")
        );

        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}