package com.example.pushwords.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPairStore;

public class SettingsTab extends AppCompatActivity {
    private View closeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_tab);

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(view -> finish());
    }
    @Override
    protected void onPause() {
        super.onPause();

        WordPairStore.getInstance(this).save();
    }
}