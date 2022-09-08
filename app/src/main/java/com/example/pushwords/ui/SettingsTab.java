package com.example.pushwords.ui;

import static com.example.pushwords.ui.NotificationIntervalTab.DEFAULT_FROM_HOUR;
import static com.example.pushwords.ui.NotificationIntervalTab.DEFAULT_FROM_MINUTE;
import static com.example.pushwords.ui.NotificationIntervalTab.DEFAULT_TO_HOUR;
import static com.example.pushwords.ui.NotificationIntervalTab.DEFAULT_TO_MINUTE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pushwords.BuildConfig;
import com.example.pushwords.R;
import com.example.pushwords.data.NotificationFrequency;
import com.example.pushwords.data.Preference;
import com.example.pushwords.data.WordPairStore;
import com.example.pushwords.ui.learnedTab.TestSettingsTab;

import java.util.TimeZone;

public class SettingsTab extends AppCompatActivity {
    private static final String ACTION_BAR_TITLE = "Настройки";

    public static final String NOTIFICATION_WORD_COUNT_PREF_NAME = "NOTIFICATION_WORD_COUNT_PREF_NAME";
    public static final int NOTIFICATION_WORD_COUNT_DEFAULT = 2;
    public static final String NOTIFICATION_FREQUENCY_INDEX_PREF_NAME = "NOTIFICATION_FREQUENCY_INDEX_PREF_NAME";
    public static final int DEFAULT_NOTIFICATION_FREQUENCY_INDEX = 0;


    private TextView timeZoneText;
    private NumberPicker notificationWordCountPicker;
    private Spinner notificationFrequencySpinner;
    private TextView notificationIntervalText;
    private View testSettingsButton;
    private View shareAppButton;


    private SharedPreferences.Editor preferencesEditor;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_tab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        // Time zone text
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

        // Notification frequency spinner
        notificationFrequencySpinner = findViewById(R.id.notificationFrequencySpinner);

        String[] notificationFrequencyDescriptions
                = new String[NotificationFrequency.values().length];
        NotificationFrequency[] notificationFrequencies = NotificationFrequency.values();
        for (int i = 0; i < notificationFrequencies.length; i++) {
            notificationFrequencyDescriptions[i] = notificationFrequencies[i].getDescription();
        }
        notificationFrequencySpinner
                .setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        notificationFrequencyDescriptions));

        int currentNotificationFrequencyIndex = preferences
                .getInt(NOTIFICATION_FREQUENCY_INDEX_PREF_NAME,
                        DEFAULT_NOTIFICATION_FREQUENCY_INDEX);
        notificationFrequencySpinner.setSelection(currentNotificationFrequencyIndex);
        notificationFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                preferencesEditor.putInt(NOTIFICATION_FREQUENCY_INDEX_PREF_NAME, index);
                preferencesEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        // Notification interval text
        notificationIntervalText = findViewById(R.id.notificationInterval);
        notificationIntervalText.setOnClickListener(view ->
                startActivity(new Intent(this, NotificationIntervalTab.class)));

        // Test settings button
        testSettingsButton = findViewById(R.id.testSettingsButton);
        testSettingsButton.setOnClickListener(view ->
                startActivity(new Intent(this, TestSettingsTab.class)));

        // Share app button
        shareAppButton = findViewById(R.id.shareAppButton);
        shareAppButton.setOnClickListener(view -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getApplicationInfo().name);
                String shareMessage= "\nРекомендую приложение\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id="
                        + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "выберите одно"));
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
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

        int fromHour = preferences.getInt(NotificationIntervalTab.FROM_HOUR_PREF_NAME, DEFAULT_FROM_HOUR);
        int fromMinute = preferences.getInt(NotificationIntervalTab.FROM_MINUTE_PREF_NAME, DEFAULT_FROM_MINUTE);
        int toHour = preferences.getInt(NotificationIntervalTab.TO_HOUR_PREF_NAME, DEFAULT_TO_HOUR);
        int toMinute = preferences.getInt(NotificationIntervalTab.TO_MINUTE_PREF_NAME, DEFAULT_TO_MINUTE);

        String intervalText = fromHour + ":" + String.format("%02d", fromMinute)
                + " - "
                + toHour + ":" + String.format("%02d", toMinute);
        notificationIntervalText.setText(intervalText);
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