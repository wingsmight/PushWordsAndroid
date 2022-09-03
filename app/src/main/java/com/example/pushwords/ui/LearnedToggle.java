package com.example.pushwords.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;

public class LearnedToggle extends FrameLayout {
    private ToggleButton toggle;

    private WordPair wordPair;


    public LearnedToggle(@NonNull Context context) {
        super(context);

        initView();
    }
    public LearnedToggle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public LearnedToggle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    public void setWordPair(WordPair wordPair) {
        this.wordPair = wordPair;

        toggle.setChecked(wordPair.state == WordPair.State.Learned);

        wordPair.setOnStateChanged(state ->
                toggle.setChecked(state == WordPair.State.Learned));
    }
    private void initView() {
        inflate(getContext(), R.layout.learned_toggle, this);

        toggle = findViewById(R.id.toggle);
        toggle.setOnClickListener(view -> {
            if (toggle.isChecked()) {
                wordPair.state = WordPair.State.Learned;
            } else {
                wordPair.state = WordPair.State.None;
            }
        });
    }
}
