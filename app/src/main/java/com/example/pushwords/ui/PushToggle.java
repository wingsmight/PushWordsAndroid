package com.example.pushwords.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;

public class PushToggle extends FrameLayout {
    private ToggleButton toggle;

    private WordPair wordPair;


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
        this.wordPair = wordPair;

        toggle.setChecked(wordPair.isPushed());

        wordPair.setOnPushedChanged(isPushed ->
                toggle.setChecked(isPushed));
    }
    private void initView() {
        inflate(getContext(), R.layout.push_toggle_button, this);

        toggle = findViewById(R.id.toggle);
        toggle.setOnClickListener(view -> wordPair.setPushed(toggle.isChecked()));
    }
}
