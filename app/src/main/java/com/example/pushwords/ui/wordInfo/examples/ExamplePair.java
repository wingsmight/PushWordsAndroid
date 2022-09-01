package com.example.pushwords.ui.wordInfo.examples;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.util.Consumer;

import com.example.pushwords.R;
import com.example.pushwords.data.Language;
import com.example.pushwords.handlers.network.TranslationApi;
import com.example.pushwords.handlers.network.dictionaryApis.examples.RussianExamplesApi;
import com.example.pushwords.handlers.network.dictionaryApis.examples.EnglishExamplesApi;
import com.example.pushwords.handlers.network.dictionaryApis.synonyms.RussianSynonymsApi;
import com.example.pushwords.handlers.network.dictionaryApis.synonyms.EnglishSynonymsApi;

import java.util.ArrayList;

public class ExamplePair extends LinearLayoutCompat {
    private TextView originalText;
    private TextView translatedText;

    private TranslationApi translationApi = new TranslationApi();


    public ExamplePair(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
    public ExamplePair(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public ExamplePair(Context context) {
        super(context);
        initView();
    }


    public void set(String originalExample, Language targetLanguage) {
        ((Activity)getContext()).runOnUiThread(() -> {
            originalText.setText(originalExample);
        });

        translationApi.translate(originalExample,
                targetLanguage.getOpposite(),
                translatedExample -> translatedText.setText(translatedExample));
    }
    public void clear() {
        originalText.setText("");
        translatedText.setText("");
    }

    private void initView() {
        inflate(getContext(), R.layout.example_pair, this);

        originalText = findViewById(R.id.original);
        translatedText = findViewById(R.id.translation);
    }
}
