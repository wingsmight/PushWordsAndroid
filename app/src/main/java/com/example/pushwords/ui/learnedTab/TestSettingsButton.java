package com.example.pushwords.ui.learnedTab;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pushwords.R;
import com.example.pushwords.ui.SettingsTab;

public class TestSettingsButton extends FrameLayout {
    private ImageButton button;


    public TestSettingsButton(@NonNull Context context) {
        super(context);

        initView();
    }
    public TestSettingsButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public TestSettingsButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.test_settings_button, this);

        button = findViewById(R.id.button);
        button.setOnClickListener(view ->
                getContext().startActivity(new Intent(getContext(), TestSettingsTab.class)));
    }
}
