package com.example.pushwords.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pushwords.R;
import com.example.pushwords.data.Language;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.WordPairViewHolder;
import com.example.pushwords.ui.wordInfo.WordInfoView;

public class WordCardTabView extends AppCompatActivity {
    private View closeButton;
    private TextView originalWordTextView;
    private WordControlPanel translatedWordControlPanel;
    private WordInfoView translatedWordInfoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.word_card);

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(view -> finish());

        originalWordTextView = findViewById(R.id.originalWordText);

        translatedWordControlPanel = findViewById(R.id.translatedWordControlPanel);
        translatedWordControlPanel.setWord(originalWordTextView);

        translatedWordInfoView = findViewById(R.id.translatedWordInfoView);

        set(getIntent().getParcelableExtra(WordPairViewHolder.WORD_PAIR_EXTRA),
                (Language) getIntent().getSerializableExtra(WordPairViewHolder.TARGET_LANGUAGE_EXTRA));
    }


    public void set(WordPair wordPair, Language targetLanguage) {
        originalWordTextView.setText(wordPair.getOriginal());
        translatedWordInfoView.setWord(wordPair.getTranslation());
        translatedWordInfoView.setTargetLanguage(targetLanguage);
    }
}
