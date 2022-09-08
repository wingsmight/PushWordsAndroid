package com.wingsmight.pushwords.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wingsmight.pushwords.R;

public class SettingsButton extends FrameLayout {
    private ImageButton button;


    public SettingsButton(@NonNull Context context) {
        super(context);

        initView();
    }
    public SettingsButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public SettingsButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.settings_button, this);

        button = findViewById(R.id.button);
        button.setOnClickListener(view ->
                getContext().startActivity(new Intent(getContext(), SettingsTab.class)));
    }
}
