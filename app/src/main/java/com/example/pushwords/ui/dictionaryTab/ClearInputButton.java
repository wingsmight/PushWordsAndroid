package com.example.pushwords.ui.dictionaryTab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pushwords.R;

public class ClearInputButton extends FrameLayout {
    private ImageButton button;
    private TextView textToClear;


    public ClearInputButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
    public ClearInputButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public ClearInputButton(Context context) {
        super(context);
        initView();
    }


    public void setTextToClear(TextView text) {
        this.textToClear = text;
    }
    private void initView() {
        inflate(getContext(), R.layout.close_button, this);

        button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            if (textToClear == null) {
                return;
            }

            textToClear.setText("");
        });
    }
}