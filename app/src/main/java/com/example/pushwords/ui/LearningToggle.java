package com.example.pushwords.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;

public class LearningToggle extends FrameLayout {
    private ToggleButton toggle;

    private WordPair wordPair;


    public LearningToggle(@NonNull Context context) {
        super(context);

        initView();
    }
    public LearningToggle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public LearningToggle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    public void setWordPair(WordPair wordPair) {
        this.wordPair = wordPair;

        toggle.setChecked(wordPair.state == WordPair.State.Learning);

        wordPair.setOnStateChanged(state ->
                toggle.setChecked(state == WordPair.State.Learning));
    }
    private void initView() {
        inflate(getContext(), R.layout.learning_toggle, this);

        toggle = findViewById(R.id.toggle);
        toggle.setOnClickListener(view -> {
            if (toggle.isChecked()) {
                wordPair.state = WordPair.State.Learning;
            } else {
                wordPair.state = WordPair.State.None;
            }
        });
    }
}
