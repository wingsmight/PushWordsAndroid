package com.wingsmight.pushwords.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairViewHolder;
import com.wingsmight.pushwords.handlers.AppCycle;
import com.wingsmight.pushwords.ui.wordInfo.WordInfoView;

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
        translatedWordControlPanel.setWordTextView(originalWordTextView);

        translatedWordInfoView = findViewById(R.id.translatedWordInfoView);

        set(getIntent().getParcelableExtra(WordPairViewHolder.WORD_PAIR_EXTRA));
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppCycle.quit(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().hide();
    }

    public void set(WordPair wordPair) {
        originalWordTextView.setText(wordPair.getOriginal().getText());
        translatedWordInfoView.setWord(wordPair.getTranslation().getText(), wordPair.getTranslationLanguage());
        translatedWordControlPanel.setWordPair(wordPair);
    }
}
