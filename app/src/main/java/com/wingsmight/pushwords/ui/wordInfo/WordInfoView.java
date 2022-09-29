package com.wingsmight.pushwords.ui.wordInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.util.Consumer;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.handlers.network.dictionaryApis.examples.IExamplesApi;
import com.wingsmight.pushwords.handlers.network.dictionaryApis.examples.RussianExamplesApi;
import com.wingsmight.pushwords.handlers.network.dictionaryApis.examples.EnglishExamplesApi;
import com.wingsmight.pushwords.handlers.network.dictionaryApis.synonyms.ISynonymsApi;
import com.wingsmight.pushwords.handlers.network.dictionaryApis.synonyms.RussianSynonymsApi;
import com.wingsmight.pushwords.handlers.network.dictionaryApis.synonyms.EnglishSynonymsApi;
import com.wingsmight.pushwords.ui.wordInfo.examples.ExampleList;
import com.wingsmight.pushwords.ui.wordInfo.synonyms.SynonymList;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.util.ArrayList;

public class WordInfoView extends LinearLayoutCompat {
    private TextView wordText;
    private SynonymList synonymList;
    private ExampleList exampleList;


    private final Consumer<ArrayList<String>> onExamplesGot = new Consumer<ArrayList<String>>() {
        @Override
        public void accept(ArrayList<String> synonyms) {
            exampleList.set(synonyms, wordLanguage);
        }
    };
    private final Consumer<ArrayList<ArrayList<String>>> onSynonymsGot = new Consumer<ArrayList<ArrayList<String>>>() {
        @Override
        public void accept(ArrayList<ArrayList<String>> exampleLists) {
            synonymList.set(exampleLists, wordLanguage);
        }
    };

    private IExamplesApi examplesApi;
    private ISynonymsApi synonymsApi;

    private Language wordLanguage = Language.Russian;


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


    public void setWord(String word, Language wordLanguage) {
        Log.i("WordInfoView", word + " from " + wordLanguage);

        setWordLanguage(wordLanguage);

        wordText.setText(word);

        synonymList.clear();
        exampleList.clear();

        synonymsApi.loadSynonyms(word, onSynonymsGot);
        examplesApi.loadExamples(word, onExamplesGot);
    }
    public void setWordLanguage(Language wordLanguage) {
        this.wordLanguage = wordLanguage;

        if (wordLanguage == Language.English) {
            synonymsApi = new EnglishSynonymsApi(getContext());
            examplesApi = new EnglishExamplesApi(getContext());
        } else if (wordLanguage == Language.Russian) {
            synonymsApi = new RussianSynonymsApi(getContext());
            examplesApi = new RussianExamplesApi(getContext());
        }
    }

    private void initView() {
        inflate(getContext(), R.layout.word_info, this);

        int elementColor = getResources().getColor(R.color.black);

        wordText = findViewById(R.id.word);
        wordText.setTextColor(elementColor);

        synonymList = findViewById(R.id.synonym_list);
        exampleList = findViewById(R.id.example_list);

        setWordLanguage(wordLanguage);

        synonymList.clear();
        exampleList.clear();

        synonymList.setTextColor(elementColor);
        exampleList.setTextColor(elementColor);

        SingleSelectToggleGroup synonymsExamplesToggle = findViewById(R.id.synonyms_examples_toggle);
        synonymsExamplesToggle.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.examples) {
                synonymList.setVisibility(GONE);
                exampleList.setVisibility(VISIBLE);
            } else {
                synonymList.setVisibility(VISIBLE);
                exampleList.setVisibility(GONE);
            }
        });
    }
}
