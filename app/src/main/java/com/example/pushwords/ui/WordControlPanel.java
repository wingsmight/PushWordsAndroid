package com.example.pushwords.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.pushwords.R;

public class WordControlPanel extends LinearLayoutCompat {
    private TextView wordView;

    private SpeakerView speaker;


    public WordControlPanel(@NonNull Context context) {
        super(context);

        initView();
    }
    public WordControlPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public WordControlPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    public void setWord(TextView wordView) {
        this.wordView = wordView;

        speaker.setSpokenTextView(wordView);
    }

    private void initView() {
        inflate(getContext(), R.layout.word_control_panel, this);

        speaker = findViewById(R.id.speakerButton);
    }
}
