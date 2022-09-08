package com.wingsmight.pushwords.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairStore;

public class LearningToggle extends FrameLayout {
    private ToggleButton toggle;

    private WordPairStore wordPairStore;
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
        this.wordPair = wordPairStore.get(wordPair);

        toggle.setChecked(wordPair.getState() == WordPair.State.Learning);

        wordPair.addOnStateChanged(state ->
                toggle.setChecked(state == WordPair.State.Learning));
    }
    private void initView() {
        inflate(getContext(), R.layout.learning_toggle, this);

        wordPairStore = WordPairStore.getInstance(getContext());

        toggle = findViewById(R.id.toggle);
        toggle.setOnClickListener(view -> {
            if (toggle.isChecked()) {
                wordPair.setState(WordPair.State.Learning);
            } else {
                wordPair.setState(WordPair.State.None);
            }
        });
    }
}
