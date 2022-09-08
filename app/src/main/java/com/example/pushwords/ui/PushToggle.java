package com.example.pushwords.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pushwords.R;
import com.example.pushwords.data.Preference;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.WordPairStore;

public class PushToggle extends FrameLayout {
    private static final String TOO_MANY_PUSHED_WORDS_ALERT = "Превышено максимальное число слов в уведомлении";


    private ToggleButton toggle;

    private WordPairStore wordPairStore;
    private WordPair wordPair;
    private SharedPreferences preference;


    public PushToggle(@NonNull Context context) {
        super(context);

        initView();
    }
    public PushToggle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public PushToggle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    public void setWordPair(WordPair wordPair) {
        this.wordPair = wordPairStore.get(wordPair);

        toggle.setChecked(wordPair.isPushed());

        wordPair.addOnPushedChanged(isPushed ->
                toggle.setChecked(isPushed));
    }
    private void initView() {
        inflate(getContext(), R.layout.push_toggle_button, this);

        wordPairStore = WordPairStore.getInstance(getContext());

        toggle = findViewById(R.id.toggle);
        toggle.setOnClickListener(view -> {
            if (getPushedWordCount() >= getNotificationWordCount()
                && toggle.isChecked()) {
                Toast.makeText(getContext(),
                        TOO_MANY_PUSHED_WORDS_ALERT,
                        Toast.LENGTH_SHORT).show();

                toggle.setChecked(false);

                return;
            }

            wordPair.setPushed(toggle.isChecked());
        });

        preference = getContext()
                .getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
    }

    private int getNotificationWordCount() {
        return preference.getInt(SettingsTab.NOTIFICATION_WORD_COUNT_PREF_NAME,
                    SettingsTab.NOTIFICATION_WORD_COUNT_DEFAULT);
    }
    private int getPushedWordCount() {
        return wordPairStore.getPushedOnly().size();
    }
}
