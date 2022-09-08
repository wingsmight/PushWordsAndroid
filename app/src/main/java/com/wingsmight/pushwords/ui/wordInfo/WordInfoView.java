package com.wingsmight.pushwords.ui.wordInfo;

import android.content.Context;
import android.util.AttributeSet;
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
            exampleList.set(synonyms, targetLanguage);
        }
    };
    private final Consumer<ArrayList<ArrayList<String>>> onSynonymsGot = new Consumer<ArrayList<ArrayList<String>>>() {
        @Override
        public void accept(ArrayList<ArrayList<String>> exampleLists) {
            synonymList.set(exampleLists, targetLanguage);
        }
    };

    private IExamplesApi examplesApi;
    private ISynonymsApi synonymsApi;

    private Language targetLanguage = Language.Russian;


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

        synonymList.clear();
        exampleList.clear();

        synonymsApi.loadSynonyms(text, onSynonymsGot);
        examplesApi.loadExamples(text, onExamplesGot);
    }
    public void setTargetLanguage(Language targetLanguage) {
        this.targetLanguage = targetLanguage;

        if (targetLanguage == Language.English) {
            synonymsApi = new EnglishSynonymsApi(getContext());
            examplesApi = new EnglishExamplesApi(getContext());
        } else if (targetLanguage == Language.Russian) {
            synonymsApi = new RussianSynonymsApi(getContext());
            examplesApi = new RussianExamplesApi(getContext());
        }
    }

    private void initView() {
        inflate(getContext(), R.layout.word_info, this);

        wordText = findViewById(R.id.word);

        synonymList = findViewById(R.id.synonym_list);
        exampleList = findViewById(R.id.example_list);

        setTargetLanguage(targetLanguage);

        synonymList.clear();
        exampleList.clear();

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
