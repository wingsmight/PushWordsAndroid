package com.example.pushwords.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.pushwords.R;

public class WordInfoView extends LinearLayoutCompat {
    private TextView wordText;


    public WordInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
    public WordInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public WordInfoView(Context context) {
        super(context);
        initView();
    }


    public void setWord(String text) {
        wordText.setText(text);
    }

    private void initView() {
        inflate(getContext(), R.layout.word_info, this);

        wordText = findViewById(R.id.word);
    }
}
