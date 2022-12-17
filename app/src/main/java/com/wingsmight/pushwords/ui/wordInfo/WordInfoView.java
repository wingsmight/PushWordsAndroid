package com.wingsmight.pushwords.ui.wordInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.util.Consumer;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.data.User;
import com.wingsmight.pushwords.data.Word;
import com.wingsmight.pushwords.handlers.PhoneticTranscription;
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
    private TextView wordTextTranscription;
    private SynonymList synonymList;
    private ExampleList exampleList;

    private PhoneticTranscription phoneticTranscription;

    private final Consumer<ArrayList<String>> onExamplesGot = new Consumer<ArrayList<String>>() {
        @Override
        public void accept(ArrayList<String> synonyms) {
            exampleList.set(synonyms, detailedWord.getLanguage());
        }
    };
    private final Consumer<ArrayList<ArrayList<String>>> onSynonymsGot = new Consumer<ArrayList<ArrayList<String>>>() {
        @Override
        public void accept(ArrayList<ArrayList<String>> exampleLists) {
            synonymList.set(exampleLists, detailedWord.getLanguage());
        }
    };

    private IExamplesApi examplesApi;
    private ISynonymsApi synonymsApi;

    private Word detailedWord = new Word("", Language.English);


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

    public void setWord(Word labelWord, Word detailedWord) {
        setWord(labelWord, detailedWord, false);
    }

    public void setWord(Word labelWord, Word detailedWord, Boolean isTranscriptionShowing) {
        setDetailedLanguage(detailedWord.getLanguage());

        wordText.setText(labelWord.getText());

        synonymList.clear();
        exampleList.clear();

        synonymsApi.loadSynonyms(detailedWord.getText(), onSynonymsGot);
        examplesApi.loadExamples(detailedWord.getText(), onExamplesGot);

        if (labelWord.getLanguage() == User.LearningLanguage &&
                isTranscriptionShowing) {
            wordTextTranscription.setText(phoneticTranscription.get(labelWord.getText()));
            wordTextTranscription.setVisibility(VISIBLE);
        } else {
            wordTextTranscription.setVisibility(GONE);
        }
    }

    public void setElementColor(int color) {
        wordText.setTextColor(color);

        synonymList.setTextColor(color);
        exampleList.setTextColor(color);
    }

    public void clear() {
        wordText.setText("");

        synonymList.clear();
        exampleList.clear();
    }

    private void initView() {
        inflate(getContext(), R.layout.word_info, this);

        wordText = findViewById(R.id.word);
        wordTextTranscription = findViewById(R.id.wordTranscription);

        synonymList = findViewById(R.id.synonym_list);
        exampleList = findViewById(R.id.example_list);

        setDetailedLanguage(detailedWord.getLanguage());

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

        phoneticTranscription = PhoneticTranscription.getInstance(getContext());
    }

    private void setDetailedLanguage(Language language) {
        if (language == Language.English) {
            synonymsApi = new EnglishSynonymsApi(getContext());
            examplesApi = new EnglishExamplesApi(getContext());
        } else if (language == Language.Russian) {
            synonymsApi = new RussianSynonymsApi(getContext());
            examplesApi = new RussianExamplesApi(getContext());
        }
    }
}
