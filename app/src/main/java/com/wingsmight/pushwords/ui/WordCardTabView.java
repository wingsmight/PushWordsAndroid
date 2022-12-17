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
import com.wingsmight.pushwords.handlers.PhoneticTranscription;
import com.wingsmight.pushwords.ui.wordInfo.WordInfoView;

public class WordCardTabView extends AppCompatActivity {
    private View closeButton;
    private TextView originalWordTextView;
    private TextView originalWordTextTranscription;
    private WordControlPanel translatedWordControlPanel;
    private WordInfoView translatedWordInfoView;

    private PhoneticTranscription phoneticTranscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.word_card);

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(view -> finish());

        originalWordTextView = findViewById(R.id.originalWordText);
        originalWordTextTranscription = findViewById(R.id.originalWordTextTranscription);

        translatedWordControlPanel = findViewById(R.id.translatedWordControlPanel);
        translatedWordControlPanel.setWordTextView(originalWordTextView);

        translatedWordInfoView = findViewById(R.id.translatedWordInfoView);

        phoneticTranscription = PhoneticTranscription.getInstance(getApplicationContext());

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
        String originalWordText = wordPair.getOriginal().getText();

        originalWordTextView.setText(originalWordText);
        translatedWordInfoView.setWord(wordPair.getTranslation(), wordPair.getOriginal());
        translatedWordControlPanel.setWordPair(wordPair);
        originalWordTextTranscription.setText(phoneticTranscription.get(originalWordText));
    }
}
