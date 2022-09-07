package com.example.pushwords.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPairStore;

public class SettingsTab extends AppCompatActivity {
    private static final String ACTION_BAR_TITLE = "Настройки";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_tab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onPause() {
        super.onPause();

        WordPairStore.getInstance(this).save();
    }
    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().setTitle(ACTION_BAR_TITLE);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return true;
    }
}