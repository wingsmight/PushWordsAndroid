package com.example.pushwords.ui.learnedTab;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pushwords.R;

public class TestSettingsTab extends AppCompatActivity {
    private View closeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_settings);

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(view -> finish());
    }
}