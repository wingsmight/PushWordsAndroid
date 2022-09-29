package com.wingsmight.pushwords.ui.wordInfo.synonyms;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.handlers.network.TranslationApi;

import java.util.ArrayList;

public class SynonymPair extends LinearLayoutCompat {
    private static final int MAX_SYNONYMS_COUNT = 3;
    private static final String EMPTY_TRANSLATED_STRING = "...";


    private TextView originalText;
    private TextView translatedText;

    private TranslationApi[] translationApis = new TranslationApi[MAX_SYNONYMS_COUNT];
    private String[] translatedSynonyms = new String[MAX_SYNONYMS_COUNT];


    public SynonymPair(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
    public SynonymPair(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public SynonymPair(Context context) {
        super(context);
        initView();
    }


    public void set(ArrayList<String> originalSynonyms, Language targetLanguage) {
        if (originalSynonyms.size() <= 0) {
            clear();

            return;
        }

        translatedSynonyms = new String[originalSynonyms.size()];
        translatedSynonyms[0] = EMPTY_TRANSLATED_STRING;

        String originalSynonymRow = originalSynonyms.get(0);
        for (int i = 1; i < originalSynonyms.size(); i++) {
            originalSynonymRow += ", " + originalSynonyms.get(i);
            translatedSynonyms[i] = EMPTY_TRANSLATED_STRING;
        }

        setTranslatedSynonyms();

        String finalOriginalSynonymRow = originalSynonymRow;
        ((Activity)getContext()).runOnUiThread(() -> {
            originalText.setText(finalOriginalSynonymRow);
        });

        Language targetTranslationLanguage = targetLanguage.getOpposite();
        for (int synonymIndex = 0; synonymIndex < originalSynonyms.size(); synonymIndex++) {
            translateSynonym(originalSynonyms.get(synonymIndex), synonymIndex, targetTranslationLanguage);
        }
    }
    public void clear() {
        originalText.setText("");
        translatedText.setText("");
    }
    public void setColor(int color) {
        originalText.setTextColor(color);
        translatedText.setTextColor(color);
    }

    private void translateSynonym(String original, int synonymIndex, Language targetLanguage) {
        translationApis[synonymIndex].translate(original,
                targetLanguage,
                translatedSynonym -> {
                    translatedSynonyms[synonymIndex] = translatedSynonym;
                    setTranslatedSynonyms();
                });
    }
    private void setTranslatedSynonyms() {
        if (translatedSynonyms.length <= 0)
            return;

        String translatedSynonymRow = translatedSynonyms[0];
        for (int i = 1; i < translatedSynonyms.length; i++) {
            String translatedSynonym = translatedSynonyms[i];
            if (translatedSynonym.isEmpty()
                || translatedSynonym.equals(EMPTY_TRANSLATED_STRING)) {
                translatedSynonym = EMPTY_TRANSLATED_STRING;
            }
            translatedSynonymRow += ", " + translatedSynonym;
        }

        String finalTranslatedSynonymRow = translatedSynonymRow;
        ((Activity)getContext()).runOnUiThread(() -> {

            translatedText.setText(finalTranslatedSynonymRow);
        });
    }
    private void initView() {
        inflate(getContext(), R.layout.synonym_pair, this);

        originalText = findViewById(R.id.original);
        translatedText = findViewById(R.id.translation);

        for (int i = 0; i < MAX_SYNONYMS_COUNT; i++) {
            translationApis[i] = new TranslationApi();
        }
    }
}
