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
import com.example.pushwords.data.WordPairStore;

public class LearnedToggle extends FrameLayout {
    private ToggleButton toggle;

    private WordPairStore wordPairStore;
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
        this.wordPair = wordPairStore.get(wordPair);

        toggle.setChecked(wordPair.getState() == WordPair.State.Learned);

        wordPair.addOnStateChanged(state ->
                toggle.setChecked(state == WordPair.State.Learned));
    }
    private void initView() {
        inflate(getContext(), R.layout.learned_toggle, this);

        wordPairStore = WordPairStore.getInstance(getContext());

        toggle = findViewById(R.id.toggle);
        toggle.setOnClickListener(view -> {
            if (toggle.isChecked()) {
                wordPair.setState(WordPair.State.Learned);
            } else {
                wordPair.setState(WordPair.State.None);
            }
        });
    }
}
