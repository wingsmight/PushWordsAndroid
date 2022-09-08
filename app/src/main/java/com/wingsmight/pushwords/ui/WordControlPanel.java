package com.wingsmight.pushwords.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairStore;

public class WordControlPanel extends LinearLayoutCompat {
    private SpeakerView speaker;
    private LearnedToggle learnedToggle;
    private LearningToggle learningToggle;
    private WordPairStore wordPairStore;


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

    public void setWordPair(WordPair wordPair) {
        wordPair = wordPairStore.get(wordPair);

        learnedToggle.setWordPair(wordPair);
        learningToggle.setWordPair(wordPair);
    }
    public void setWordTextView(TextView wordView) {
        speaker.setSpokenTextView(wordView);
    }

    private void initView() {
        inflate(getContext(), R.layout.word_control_panel, this);

        speaker = findViewById(R.id.speakerButton);
        learnedToggle = findViewById(R.id.learnedToggle);
        learningToggle = findViewById(R.id.learningToggle);

        wordPairStore = WordPairStore.getInstance(getContext());
    }
}
