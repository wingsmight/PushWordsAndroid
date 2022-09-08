package com.example.pushwords.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pushwords.R;
import com.example.pushwords.data.Preference;
import com.example.pushwords.data.WordPairStore;

import java.util.TimeZone;

public class SettingsTab extends AppCompatActivity {
    private static final String ACTION_BAR_TITLE = "Настройки";

    public static final String NOTIFICATION_WORD_COUNT_PREF_NAME = "NOTIFICATION_WORD_COUNT_PREF_NAME";
    public static final int NOTIFICATION_WORD_COUNT_DEFAULT = 2;


    private TextView timeZoneText;
    private NumberPicker notificationWordCountPicker;


    private SharedPreferences.Editor preferencesEditor;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_tab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        timeZoneText = findViewById(R.id.timeZoneText);
        TimeZone timeZone = TimeZone.getDefault();
        String timeZoneDisplayName = timeZone.getDisplayName(false, TimeZone.SHORT)
            + " " + timeZone.getID();
        timeZoneText.setText(timeZoneDisplayName);

        notificationWordCountPicker = findViewById(R.id.notificationWordCountPicker);
        initPicker(notificationWordCountPicker,
                NOTIFICATION_WORD_COUNT_PREF_NAME,
                NOTIFICATION_WORD_COUNT_DEFAULT,
                1,
                4);
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

    // TODO: fix repeating from TestSettingsTab
    private void initPicker(NumberPicker picker,
                            String name,
                            int defaultValue,
                            int minValue,
                            int maxValue) {
        int value = preferences.getInt(name, defaultValue);

        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(value);
        picker.setOnValueChangedListener((numberPicker, i, i1) ->
                preferencesEditor
                        .putInt(name, numberPicker.getValue())
                        .apply());
    }
}