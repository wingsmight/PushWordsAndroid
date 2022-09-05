package com.example.pushwords.ui.learnedTab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pushwords.R;
import com.example.pushwords.data.Preference;
import com.example.pushwords.data.WordPairStore;

public class TestSettingsTab extends AppCompatActivity {
    private static final String LAST_LEARNED_WORD_COUNT_PREF_NAME = "LAST_LEARNED_WORD_COUNT_PREF_NAME";
    private static final String LEARNED_WORD_COUNT_PREF_NAME = "LEARNED_WORD_COUNT_PREF_NAME";
    private static final String FORGOT_REPEAT_COUNT_PREF_NAME = "FORGOT_REPEAT_COUNT";
    private static final String REVERSE_TRANSLATION_PREF_NAME = "REVERSE_TRANSLATION_PREF_NAME";

    private View closeButton;
    private NumberPicker lastLearnedCountPicker;
    private NumberPicker learnedCountPicker;
    private NumberPicker forgotRepeatPicker;
    private ToggleButton reverseTranslationToggle;

    private SharedPreferences.Editor preferencesEditor;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_settings);

        preferences = getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(view -> finish());

        lastLearnedCountPicker = findViewById(R.id.lastLearnedCountPicker);
        initPicker(lastLearnedCountPicker,
                LAST_LEARNED_WORD_COUNT_PREF_NAME,
                20,
                1,
                40);

        learnedCountPicker = findViewById(R.id.learnedCountPicker);
        initPicker(learnedCountPicker,
                LEARNED_WORD_COUNT_PREF_NAME,
                15,
                1,
                30);

        forgotRepeatPicker = findViewById(R.id.forgotRepeatPicker);
        initPicker(forgotRepeatPicker,
                FORGOT_REPEAT_COUNT_PREF_NAME,
                3,
                1,
                10);

        reverseTranslationToggle = findViewById(R.id.reverseTranslationToggle);
        reverseTranslationToggle
                .setChecked(preferences.getBoolean(REVERSE_TRANSLATION_PREF_NAME, true));
        reverseTranslationToggle.setOnClickListener(view -> {
            preferencesEditor.putBoolean(REVERSE_TRANSLATION_PREF_NAME,
                    reverseTranslationToggle.isChecked())
                    .apply();
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        WordPairStore.getInstance(this).save();
    }


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