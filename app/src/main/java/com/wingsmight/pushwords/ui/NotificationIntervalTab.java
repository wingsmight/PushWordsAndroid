package com.wingsmight.pushwords.ui;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.WordPairStore;

public class NotificationIntervalTab extends AppCompatActivity {
    public static final int DEFAULT_FROM_HOUR = 8;
    public static final int DEFAULT_FROM_MINUTE = 0;
    public static final int DEFAULT_TO_HOUR = 22;
    public static final int DEFAULT_TO_MINUTE = 0;
    public static final String FROM_HOUR_PREF_NAME = "FROM_HOUR_PREF_NAME";
    public static final String FROM_MINUTE_PREF_NAME = "FROM_MINUTE_PREF_NAME";
    public static final String TO_HOUR_PREF_NAME = "TO_HOUR_PREF_NAME";
    public static final String TO_MINUTE_PREF_NAME = "TO_MINUTE_PREF_NAME";


    private int fromHour = DEFAULT_FROM_HOUR;
    private int fromMinute = DEFAULT_FROM_MINUTE;
    private int toHour = DEFAULT_TO_HOUR;
    private int toMinute = DEFAULT_TO_MINUTE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_interval_tab);

        SharedPreferences preferences = getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        fromHour = preferences.getInt(FROM_HOUR_PREF_NAME, fromHour);
        fromMinute = preferences.getInt(FROM_MINUTE_PREF_NAME, fromMinute);
        toHour = preferences.getInt(TO_HOUR_PREF_NAME, toHour);
        toMinute = preferences.getInt(TO_MINUTE_PREF_NAME, toMinute);

        TextView fromTimeText = findViewById(R.id.fromTime);
        fromTimeText.setText(fromHour + ":" + String.format("%02d", fromMinute));
        fromTimeText.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                fromTimeText.setText(hourOfDay + ":" + String.format("%02d", minute));
                fromHour = hourOfDay;
                fromMinute = minute;
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    timeSetListener,
                    fromHour,
                    fromMinute,
                    true);
            timePickerDialog.show();
        });

        TextView toTimeText = findViewById(R.id.toTime);
        toTimeText.setText(toHour + ":" + String.format("%02d", toMinute));
        toTimeText.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener timeSetListener = (view12, hourOfDay, minute) -> {
                toTimeText.setText(hourOfDay + ":" + String.format("%02d", minute));
                toHour = hourOfDay;
                toMinute = minute;
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    timeSetListener,
                    toHour,
                    toMinute,
                    true);
            timePickerDialog.show();
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(view -> finish());

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> {
            SharedPreferences.Editor preferenceEditor = preferences.edit();

            preferenceEditor.putInt(FROM_HOUR_PREF_NAME, fromHour);
            preferenceEditor.putInt(FROM_MINUTE_PREF_NAME, fromMinute);
            preferenceEditor.putInt(TO_HOUR_PREF_NAME, toHour);
            preferenceEditor.putInt(TO_MINUTE_PREF_NAME, toMinute);

            preferenceEditor.commit();

            finish();
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        WordPairStore.getInstance(this).save();
    }
}